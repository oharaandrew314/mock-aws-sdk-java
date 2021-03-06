package io.andrewohara.awsmock.ssm

import com.amazonaws.services.simplesystemsmanagement.model.DeleteParametersRequest
import io.andrewohara.awsmock.ssm.SsmUtils.set
import io.andrewohara.awsmock.ssm.SsmUtils.delete
import io.andrewohara.awsmock.ssm.SsmUtils.get
import org.assertj.core.api.Assertions.*
import org.junit.After
import org.junit.Test

class DeleteParametersTest {

    private val client = MockAWSSimpleSystemsManagement()

    @After
    fun cleanup() {
        client.delete("name")
    }

    @Test
    fun `delete missing parameters`() {
        val resp = client.deleteParameters(DeleteParametersRequest().withNames("name"))

        assertThat(resp.deletedParameters).isEmpty()
        assertThat(resp.invalidParameters).containsExactly("name")
    }

    @Test
    fun `delete parameters`() {
        client["name"] = "Andrew"

        val resp = client.deleteParameters(DeleteParametersRequest().withNames("name"))

        assertThat(resp.deletedParameters).containsExactly("name")
        assertThat(resp.invalidParameters).isEmpty()
        assertThat(client["name"]).isNull()
    }
}