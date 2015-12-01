definition(
name: "door_sensor_light_event",
namespace: "owrangm",
author: "Maryam",
description: "Light Control with Sensor",
category: "Convenience",
iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
oauth: [displayName: "web services tutorial ", displayLink: "http://localhost:4567"])
preferences {
  section ("Allow external service to control these things...") {
    input "switches", "capability.switch", multiple: true, required: true
  }
  section("Monitor this door or window") {
    input "contacts", "capability.contactSensor"
  }
}


mappings {

  path("/switches") {
    action: [
      GET: "listSwitches"
    ]
  }

  path("/contacts") {
    action: [
      GET: "listContactSensors"
    ]
  }

  path("/switches/:command") {
    action: [
      PUT: "updateSwitches"
    ]
  }

}

// returns a list like
// [[name: "kitchen lamp", value: "off"], [name: "bathroom", value: "on"]]
def listSwitches() {
  def resp = []
  switches.each {
    resp << [name: it.displayName, value: it.currentValue("switch")]
  }
  return resp
}

def listContactSensors() {
  def resp = []
  contacts.each {
    resp << [name: it.displayName, value: it.currentValue("contact")]
  }
  return resp
}

void updateSwitches() {
  // use the built-in request object to get the command parameter
  def command = params.command
  if (command) {
  // check that the switch supports the specified command
  // If not, return an error using httpError, providing a HTTP status code.
  switches.each {
    if (!it.hasCommand(command)) {
    httpError(501, "$command is not a valid command for all switches specified")
    }
  }
  // all switches have the comand
  // execute the command on all switches
  // (note we can do this on the array - the command will be invoked on every element
  switches."$command"()
  }
}

def doorSensorHandler(evt) {
    log.debug "door state changed!"
    log.debug "event name: ${evt.name}"
    log.debug "event value: ${evt.value}"
    // call CCE.event 
    
    def params = [
      uri: "http://216.23.170.132:4567", 
      path: "/event"
    ]
    
    try 
    { 
      httpGet(params) { resp -> 
        resp.headers.each {
          log.debug "${it.name} : ${it.value}"
          }
          log.debug "response contentType: ${resp.contentType}"
        }
    } catch(e) { 
      log.error "could not post to localhost"
    }
    
}

def installed() {

  log.debug "Installed with settings: ${settings}"
  subscribe(contacts, "contact", doorSensorHandler )
 
}

def updated() {
  subscribe(contacts, "contact", doorSensorHandler )
}