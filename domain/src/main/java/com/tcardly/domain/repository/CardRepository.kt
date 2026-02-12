package com.tcardly.domain.repository

import com.tcardly.core.common.model.ResultWrapper
import com.tcardly.domain.model.BusinessCard
import kotlinx.coroutines.flow.Flow

interface CardRepository {
    fun getAllCards(): Flow<List<BusinessCard>>
    fun getRecentCards(limit: Int = 5): Flow<List<BusinessCard>>
    fun searchCards(query: String): Flow<List<BusinessCard>>
    fun getCardsByGroup(groupId: Long): Flow<List<BusinessCard>>
    fun getFavoriteCards(): Flow<List<BusinessCard>>
    fun observeCard(cardId: Long): Flow<BusinessCard?>
    fun getCardCount(): Flow<Int>
    suspend fun getCardById(cardId: Long): BusinessCard?
    suspend fun saveCard(card: BusinessCard): ResultWrapper<Long>
    suspend fun updateCard(card: BusinessCard): ResultWrapper<Unit>
    suspend fun deleteCard(cardId: Long): ResultWrapper<Unit>
    suspend fun toggleFavorite(cardId: Long, isFavorite: Boolean): ResultWrapper<Unit>
    suspend fun findDuplicate(name: String, phone: String): BusinessCard?
    suspend fun exportToCsv(): ResultWrapper<String>
    suspend fun generateVCard(cardId: Long): ResultWrapper<String>
}
