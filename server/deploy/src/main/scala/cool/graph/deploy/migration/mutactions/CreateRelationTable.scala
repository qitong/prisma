package cool.graph.deploy.migration.mutactions

import cool.graph.deploy.database.DatabaseMutationBuilder
import cool.graph.shared.models.{Schema, Relation}

import scala.concurrent.Future

case class CreateRelationTable(projectId: String, schema: Schema, relation: Relation) extends ClientSqlMutaction {
  override def execute: Future[ClientSqlStatementResult[Any]] = {

    val aModel = schema.getModelById_!(relation.modelAId)
    val bModel = schema.getModelById_!(relation.modelBId)

    Future.successful(
      ClientSqlStatementResult(
        sqlAction = DatabaseMutationBuilder
          .createRelationTable(projectId = projectId, tableName = relation.id, aTableName = aModel.name, bTableName = bModel.name)))
  }

  override def rollback = Some(DeleteRelationTable(project, relation).execute)
}