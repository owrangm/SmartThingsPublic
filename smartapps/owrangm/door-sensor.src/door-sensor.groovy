definition(
name: "door_sensor",
namespace: "owrangm",
author: "Maryam",
description: "door sensor",
category: "Convenience",
//parent: "owrangm: CCE",
iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
oauth: [displayName: "web services tutorial ", displayLink: "http://localhost:4567"])
preferences {
  section("Monitor this door or window") {
    input "contacts", "capability.contactSensor"
  }
}

def listContactSensors() {
  def resp = []
  contacts.each {
    resp << [name: it.displayName, value: it.currentValue("contact")]
  }
  return resp
}

def installed() {}

def updated() {}