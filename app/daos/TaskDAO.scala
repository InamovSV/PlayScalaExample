package daos

import models.Task
import com.mongodb.ConnectionString
import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}
import org.bson.codecs.configuration.CodecRegistry
import org.mongodb.scala.bson.ObjectId
import org.mongodb.scala.bson.codecs.DEFAULT_CODEC_REGISTRY
import org.mongodb.scala.bson.codecs.Macros._
import org.mongodb.scala.connection.NettyStreamFactoryFactory

import org.mongodb.scala.model.Filters._
import org.mongodb.scala.model.Updates._
import org.mongodb.scala.{MongoClient, MongoClientSettings, MongoCollection}

//mongodb://testUser:1234@cluster0-shard-00-00-htlg0.mongodb.net:27017,cluster0-shard-00-01-htlg0.mongodb.net:27017,cluster0-shard-00-02-htlg0.mongodb.net:27017/test?ssl=true&replicaSet=Cluster0-shard-0&authSource=admin&retryWrites=true

object TaskDAO extends Helpers {
  def apply(_id: ObjectId,
            label: String,
            who: String,
            mytime: String,
            ready: Short) = Task(_id, label, who, mytime, ready)

//  val uri = "mongodb://testUser:1234@cluster0-shard-00-00-htlg0.mongodb.net:27017,cluster0-shard-00-01-htlg0.mongodb.net:27017,cluster0-shard-00-02-htlg0.mongodb.net:27017/test?ssl=true&streamType=netty&replicaSet=Cluster0-shard-0&authSource=admin&retryWrites=true"
  val uri = "mongodb://testUser:1234@cluster0-shard-00-00-htlg0.mongodb.net:27017,cluster0-shard-00-01-htlg0.mongodb.net:27017,cluster0-shard-00-02-htlg0.mongodb.net:27017/test?ssl=true&replicaSet=Cluster0-shard-0&authSource=admin&retryWrites=true"
  val dbName = "Cluster0"
  val collectionName = "tasks1"

  val mongoClientSettings = MongoClientSettings.builder()
    .applyConnectionString(new ConnectionString(uri))
    .streamFactoryFactory(NettyStreamFactoryFactory())
    .applyToSslSettings(b => b.enabled(true))
    .build()
  val mongoClient = MongoClient(mongoClientSettings)
  val companyCodecRegistry: CodecRegistry = fromRegistries(fromProviders(classOf[Task]), DEFAULT_CODEC_REGISTRY)
  val database = mongoClient.getDatabase(dbName).withCodecRegistry(companyCodecRegistry)
  val companyCollection: MongoCollection[Task] = database.getCollection(collectionName)

  def create(task: Task) =
    companyCollection
      .insertOne(task)
      .printHeadResult("Insert result = ")

  def createMany(companies: Seq[Task]) =
    companyCollection
      .insertMany(companies)
      .printHeadResult("Insert (many) result = ")

  def update(companyId: ObjectId, fieldToUpdate: String, newValue: Any) =
    companyCollection
      .updateOne(equal("_id", companyId), set(fieldToUpdate, newValue))
      .printHeadResult("Update result = ")

  def updateMany(fieldToIdentify: String, identifier: Any, fieldToUpdate: String, newValue: Any) =
    companyCollection
      .updateMany(equal(fieldToIdentify, identifier), set(fieldToUpdate, newValue))
      .printHeadResult("Update (many) result = ")

  def delete(companyId: ObjectId) =
    companyCollection
      .deleteOne(equal("_id", companyId))
      .printHeadResult("Delete result = ")

  def deleteAll(): Unit = companyCollection.drop()

  def find(companyId: ObjectId) =
    companyCollection
      .find(equal("_id", companyId))
      .first()
      .headResult()

  def findAll(): Seq[Task] = companyCollection.find().results()

  def printAll(): Unit = findAll() foreach println
}
