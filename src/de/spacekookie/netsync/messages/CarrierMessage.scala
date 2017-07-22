package de.spacekookie.netsync.messages

import java.util.Date
import scala.collection.mutable.HashMap

/**
 * A base message which can be used to carry a variety of commands and sub-messages
 */
class CarrierMessage(val msgID: Long, val time: Date, val cmd: MessageType.Value) {
  val payloads: HashMap[PayloadType.Value, AnyVal] = new HashMap()

  /**
   * Small utility function which will set the payload on a message
   *
   * @param t: The payload type described by #PayloadType.Value
   * @param payload: Any type of data to send. Needs to be registered
   */
  def addPayload(t: PayloadType.Value, payload: Unit) = {
    if (payloads.contains(t))
      println(s"[WARN]: Payload type $t is being overriden")
    this.payloads.put(t, payload)
  }
}
