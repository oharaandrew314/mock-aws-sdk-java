package io.andrewohara.awsmock.dynamodb

import com.amazonaws.services.dynamodbv2.model.*
import io.andrewohara.awsmock.dynamodb.TestUtils.eq
import org.assertj.core.api.Assertions.*
import org.junit.Test

class QueryUnitTest {

    private val client = MockAmazonDynamoDB()

    @Test
    fun `query table without sort key`() {
        CatsFixtures.createCatsTableByName(client)

        client.putItem(CatsFixtures.tableName, CatsFixtures.smokey)
        client.putItem(CatsFixtures.tableName, CatsFixtures.bandit)
        client.putItem(CatsFixtures.tableName, CatsFixtures.toggles)

        val request = QueryRequest()
                .withTableName(CatsFixtures.tableName)
                .withKeyConditions(mapOf("name" to Condition().eq("Toggles")))
        val result = client.query(request)

        assertThat(result.items).containsExactly(CatsFixtures.toggles)
    }

    @Test
    fun `query table with sort key`() {
        CatsFixtures.createCatsTableByOwnerIdAndName(client)

        client.putItem(CatsFixtures.tableName, CatsFixtures.smokey)
        client.putItem(CatsFixtures.tableName, CatsFixtures.bandit)
        client.putItem(CatsFixtures.tableName, CatsFixtures.toggles)

        val request = QueryRequest()
                .withTableName(CatsFixtures.tableName)
                .withKeyConditions(mapOf("ownerId" to Condition().eq(1)))
        val result = client.query(request)

        assertThat(result.items).containsExactly(CatsFixtures.bandit, CatsFixtures.smokey)
    }
}