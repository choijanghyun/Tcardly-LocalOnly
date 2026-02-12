package com.tcardly.domain.model

data class AiAnalysisReport(
    val companyName: String,
    val businessOutlook: String = "",
    val swot: SwotAnalysis? = null,
    val investmentAttractiveness: Int = 0,  // 0~100
    val competitorComparison: String = "",
    val riskIssues: String = "",
    val aiSummary: String = "",
    val generatedAt: Long = 0
)

data class SwotAnalysis(
    val strengths: List<String> = emptyList(),
    val weaknesses: List<String> = emptyList(),
    val opportunities: List<String> = emptyList(),
    val threats: List<String> = emptyList()
)
