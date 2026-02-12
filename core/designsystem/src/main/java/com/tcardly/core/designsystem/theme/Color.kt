package com.tcardly.core.designsystem.theme

import androidx.compose.ui.graphics.Color

// T-CARDLY Brand Colors - "Transformative Teal"
object TCardlyColors {
    // Primary Teal
    val TealDeep = Color(0xFF0D9488)       // Primary: 주요 버튼, CTA, 활성 탭
    val TealMid = Color(0xFF2DD4BF)        // Light: 그라데이션 끝점, 호버
    val TealSoft = Color(0xFFCCFBF1)       // Surface: 카드 배경, 태그 배경

    // Neutral / Background
    val CloudBase = Color(0xFFFAF9F7)      // 전체 앱 배경 "Cloud Dancer"
    val PureWhite = Color(0xFFFFFFFF)      // 카드, 입력필드, 모달 배경

    // Semantic
    val CoralWarm = Color(0xFFF97066)      // 경고: 핫리드 태그, 삭제, 에러
    val Emerald = Color(0xFF10B981)        // 성공: 토스트, 높은 신뢰도
    val Amber = Color(0xFFF59E0B)          // 주의: 팔로업 태그, 낮은 신뢰도
    val Sky = Color(0xFF38BDF8)            // 정보: 신규 태그, 정보 배지

    // Text
    val SlateDeep = Color(0xFF1E293B)      // 제목, 이름, 본문 핵심 텍스트
    val SlateMid = Color(0xFF64748B)       // 부제, 직함, 날짜, 설명 텍스트
    val SlateLight = Color(0xFF94A3B8)     // 플레이스홀더, 비활성 라벨

    // Border
    val BorderSoft = Color(0xFFE8E6E3)     // 카드 테두리, 구분선

    // Dark Theme Variants
    val DarkBackground = Color(0xFF0F172A)
    val DarkSurface = Color(0xFF1E293B)
    val DarkCard = Color(0xFF334155)
}
