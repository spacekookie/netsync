package de.spacekookie.netsync.messages

/** Describes the type of payload that is being included */
object PayloadType extends Enumeration {

  /** Data the client wants to share about itself (metadata) */
  val CLIENT_DATA = Value

  /** Data that needs to be actally synced (PRIMARY PAYLOAD) */
  val SYNC_DATA = Value

  /** Metadata the server can share with clients (if required) */
  val SERVER_DATA = Value

  /** Conflict Resolve data (server wants to resolve client errors */
  val CR_DATA = Value
}
 