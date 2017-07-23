package de.spacekookie.netsync

import java.net.SocketTimeoutException
import java.util.Date

import scala.collection.mutable.Queue

import com.esotericsoftware.kryo.Kryo
import com.esotericsoftware.kryonet.Client

import de.spacekookie.netsync.messages.CarrierMessage
import de.spacekookie.netsync.messages.MessageType.Value
import de.spacekookie.netsync.messages.MessageType
import de.spacekookie.netsync.messages.PayloadType.Value
import de.spacekookie.netsync.messages.PayloadType

import scala.collection.mutable.HashMap
import java.io.IOException
import com.esotericsoftware.minlog.Log

/**
 * A simple enum class that communicates error state
 */
object ClientCodes extends Enumeration {
  type ClientCodes = Value

  /** The connection went through successfully */
  val STATUS_SUCCESS = Value

  /** The server suddenly dropped the client */
  val STATUS_ERROR_DROPPED = Value

  /** The client failed to make a connection */
  val STATUS_ERROR_CONNECT = Value

  /** Connection failed because of a timeout  */
  val STATUS_ERROR_TIMEOUT = Value

  /** Connection failed permanently because "tries" was reached */
  val STATUS_ERROR_PERMAFAIL = Value
}

/**
 * Creates a new network client for netsync. Provide a timeout time
 * and optionally a "tries" variable which will determine how many times
 * the client will try to establish a connection before permanently
 * failing and running the callback function with the appropriate error
 * code.
 *
 * @param timeout The amount of time before calling a timeout
 * @param tries After how many times will the client give up? (Default: -1 infinitely)
 */
class NetClient(val timeout: Int, val tries: Int = -1) {
  import ClientCodes._

  val client: Client = new Client()
  val cmdBuffer: Queue[() => Unit] = new Queue[() => Unit]()
  val self = this

  /** Disable the spammy Kryo log */
  Log.set(Log.LEVEL_NONE)

  /** The NetClient runs asynchronously most of the time */
  new Thread(new Runnable {
    def run() {
      self.synchronized {
        var running = true

        while (running) {

          /** Wait until we are summoned */
          self.wait(timeout)

          /** Run all queued commands */
          for (cmd <- cmdBuffer)
            cmd()

          /** Clear the current command buffer */
          cmdBuffer.clear()

        }
      } /* Synchronised */

    }
  }).start()

  /** Register our own datatypes that are needed */
  register(classOf[CarrierMessage])
  register(classOf[MessageType.Value])
  register(classOf[PayloadType.Value])
  register(classOf[HashMap[_, _]])
  register(classOf[Date])

  /**
   * Connect to a server target via an address and port provided. You
   * can also provide an optional callback handle to be executed with
   * a connect status either when the connection is established or
   * when the connection attempts have timed out or permanently failed.
   *
   * A function doesn't have to be provided but if it is, should be able
   * to handle both success and failure cases for further processing.
   *
   * @param target: The address of the server target
   * @param port: The port the server listens to
   * @param callback: An optional callback function
   */
  def connect(target: String, port: Int, callback: Option[ClientCodes => Unit]) = {
    this.synchronized {
      cmdBuffer.enqueue { () =>

        /** Always assume the best */
        var ret: ClientCodes = STATUS_SUCCESS

        try {
          client.connect(5000, target, port, port)
        } catch {
          case t: SocketTimeoutException => ret = STATUS_ERROR_TIMEOUT
          case t: IOException => ret = STATUS_ERROR_CONNECT
        }

        /** Only execute callback if it exists */
        callback match {

          /** Run the provided callback on a non-blocking thread */
          case Some(callback) => new Thread(new Runnable {
            def run() {
              callback(ret)
            }
          }).start()

          // TODO: We might internally consume the return value somehow?
          case none =>
        }
      }

      /** Wake up the wait() next door */
      this.notify()
    }
  }

  /**
   * Disconnect from the currently connected server. Will do nothing if no
   * server connection is currently active. An optional callback function can
   * be provided to execute code after a successful disconnect or when the
   * disconnect step has permanently failed.
   */
  def disconnect(callback: (Int) => Option[Unit]) = {

  }

  /**
   * Register a new object and all of it's children that are required
   * in order to properly serialise it. This function is pretty slow
   * and shouldn't be called inside your standard run loop
   *
   * @param obj: An object you want to register
   */
  def register(clazz: Class[_]) = {
    val kryo: Kryo = client.getKryo()
    kryo.register(clazz)
  }
}