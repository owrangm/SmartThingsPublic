definition(
    name: "Cce",
    namespace: "owrangm",
    author: "Maryam Owrang",
    description: "An example of getting multiple devices and controling them",
    category: "My Apps",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png")

preferences {
  // The parent app preferences are pretty simple: just use the app input for the child app.
  page(name: "mainPage", title: "Simple Automations", install: true, uninstall: true, submitOnChange: true) {
    section {
      app(name: "door_sensor", appName: "door_sensor", namespace: "owrangm", title: "link to CCE devices", multiple: true)
    }
  }
  section("Monitor this door or window") {
    input "contacts", "capability.contactSensor"
  }
}

mappings {
  path("/contacts") {
    action: [
      GET: "listContacts"
    ]
  }
}

def listContacts()
{ 
  def resp = []
  def theChild = findChildAppByName("door_sensor")
  log.debug "Child app id: ${theChild.id}"
  //resp << theChild.id
  //return resp 
  contactSensors = theChild.listContactSensors()
  contactSensors.each {
    resp << [name: it.name, value: it.value]
  }  
  
  //return theChild.id 
  //'Hello World'
}

def installed() {

  log.debug "Installed with settings: ${settings}"
  initialize()

}

def updated() {

  log.debug "Updated with settings: ${settings}"
  initialize()

}

def initialize() {

  // nothing needed here, since the child apps will handle preferences/subscriptions
  // this just logs some messages for demo/information purposes
  //log.debug "there are ${childApps.size()} child smartapps"
  //childApps.each {child ->
  //    log.debug "child app: ${child.label}"
  //}

}