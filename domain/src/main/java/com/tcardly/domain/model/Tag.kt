package com.tcardly.domain.model

data class Tag(
    val id: Long = 0,
    val name: String,
    val bgColor: String,
    val textColor: String,
    val isDefault: Boolean = false
) {
    companion object {
        val HOT_LEAD = Tag(name = "핫리드", bgColor = "#FEE2E2", textColor = "#DC2626", isDefault = true)
        val FOLLOW_UP = Tag(name = "팔로업", bgColor = "#FEF3C7", textColor = "#D97706", isDefault = true)
        val NEW = Tag(name = "신규", bgColor = "#DBEAFE", textColor = "#2563EB", isDefault = true)
        val GENERAL = Tag(name = "일반", bgColor = "#F1F5F9", textColor = "#64748B", isDefault = true)
    }
}
