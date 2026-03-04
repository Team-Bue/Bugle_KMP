package team.bue.bugle.core.network.dto.report

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateReportRequestDto(
    val reason: String,
    @SerialName("report_type")
    val reportType: ReportTypeDto,
)

@Serializable
enum class ReportTypeDto {
    @SerialName("POST")
    POST,

    @SerialName("COMMENT")
    COMMENT,
}
