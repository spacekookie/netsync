package de.spacekookie.netsync.messages

/** Enumerable that defines the type of message */
object MessageType extends Enumeration {

  /** A client joins the network and tells it's lifes story */
  val ANNOUNCE = Value

  /** A client wants to leave the network */
  val QUIT = Value

  /** A normal update message with a primary payload */
  val UPDATE = Value

  /** A query for changes to either the server or a client */
  val QUERY = Value

  /** A client had problems applying a change from the server */
  val CONFLICT = Value

  /** A fatal error has occured and the network is going down */
  val FATAL = Value
}