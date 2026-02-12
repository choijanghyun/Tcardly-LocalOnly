package com.tcardly.data.repository

import com.tcardly.core.common.model.ResultWrapper
import com.tcardly.core.common.util.DateUtils
import com.tcardly.core.database.dao.BusinessCardDao
import com.tcardly.core.database.dao.ActivityLogDao
import com.tcardly.core.database.entity.ActivityLogEntity
import com.tcardly.core.common.model.ActivityType
import com.tcardly.data.mapper.toDomain
import com.tcardly.data.mapper.toEntity
import com.tcardly.domain.model.BusinessCard
import com.tcardly.domain.repository.CardRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CardRepositoryImpl @Inject constructor(
    private val cardDao: BusinessCardDao,
    private val activityLogDao: ActivityLogDao
) : CardRepository {

    override fun getAllCards(): Flow<List<BusinessCard>> =
        cardDao.getAllCards().map { list -> list.map { it.toDomain() } }

    override fun getRecentCards(limit: Int): Flow<List<BusinessCard>> =
        cardDao.getRecentCards(limit).map { list -> list.map { it.toDomain() } }

    override fun searchCards(query: String): Flow<List<BusinessCard>> =
        cardDao.searchCards(query).map { list -> list.map { it.toDomain() } }

    override fun getCardsByGroup(groupId: Long): Flow<List<BusinessCard>> =
        cardDao.getCardsByGroup(groupId).map { list -> list.map { it.toDomain() } }

    override fun getFavoriteCards(): Flow<List<BusinessCard>> =
        cardDao.getFavoriteCards().map { list -> list.map { it.toDomain() } }

    override fun observeCard(cardId: Long): Flow<BusinessCard?> =
        cardDao.observeCardById(cardId).map { it?.toDomain() }

    override fun getCardCount(): Flow<Int> = cardDao.getCardCount()

    override suspend fun getCardById(cardId: Long): BusinessCard? =
        cardDao.getCardById(cardId)?.toDomain()

    override suspend fun saveCard(card: BusinessCard): ResultWrapper<Long> {
        return try {
            val id = cardDao.insertCard(card.toEntity())
            activityLogDao.insert(
                ActivityLogEntity(
                    cardId = id,
                    type = when (card.sourceType) {
                        com.tcardly.core.common.model.SourceType.CAMERA_OCR -> ActivityType.SCAN
                        com.tcardly.core.common.model.SourceType.QR_CODE -> ActivityType.QR_SCAN
                        com.tcardly.core.common.model.SourceType.MANUAL -> ActivityType.MANUAL_INPUT
                        else -> ActivityType.SCAN
                    },
                    content = "명함 등록",
                    timestamp = DateUtils.now()
                )
            )
            Timber.d("명함 저장 성공: id=$id, name=${card.name}")
            ResultWrapper.Success(id)
        } catch (e: Exception) {
            Timber.e(e, "명함 저장 실패: name=${card.name}")
            ResultWrapper.Error("명함 저장 실패: ${e.message}", e)
        }
    }

    override suspend fun updateCard(card: BusinessCard): ResultWrapper<Unit> {
        return try {
            cardDao.updateCard(card.toEntity())
            Timber.d("명함 수정 성공: id=${card.id}")
            ResultWrapper.Success(Unit)
        } catch (e: Exception) {
            Timber.e(e, "명함 수정 실패: id=${card.id}")
            ResultWrapper.Error("명함 수정 실패: ${e.message}", e)
        }
    }

    override suspend fun deleteCard(cardId: Long): ResultWrapper<Unit> {
        return try {
            cardDao.deleteCard(cardId)
            Timber.d("명함 삭제 성공: id=$cardId")
            ResultWrapper.Success(Unit)
        } catch (e: Exception) {
            Timber.e(e, "명함 삭제 실패: id=$cardId")
            ResultWrapper.Error("명함 삭제 실패: ${e.message}", e)
        }
    }

    override suspend fun toggleFavorite(cardId: Long, isFavorite: Boolean): ResultWrapper<Unit> {
        return try {
            cardDao.toggleFavorite(cardId, isFavorite, DateUtils.now())
            Timber.d("즐겨찾기 변경: id=$cardId, isFavorite=$isFavorite")
            ResultWrapper.Success(Unit)
        } catch (e: Exception) {
            Timber.e(e, "즐겨찾기 변경 실패: id=$cardId")
            ResultWrapper.Error("즐겨찾기 변경 실패", e)
        }
    }

    override suspend fun findDuplicate(name: String, phone: String): BusinessCard? =
        cardDao.findDuplicate(name, phone)?.toDomain()

    override suspend fun exportToCsv(): ResultWrapper<String> {
        // CSV 내보내기 구현 (파일 생성 후 URI 반환)
        return ResultWrapper.Success("export_path")
    }

    override suspend fun generateVCard(cardId: Long): ResultWrapper<String> {
        val card = cardDao.getCardById(cardId) ?: return ResultWrapper.Error("명함을 찾을 수 없습니다.")
        val vCard = buildString {
            appendLine("BEGIN:VCARD")
            appendLine("VERSION:3.0")
            appendLine("FN:${card.name}")
            card.company?.let { appendLine("ORG:$it") }
            card.position?.let { appendLine("TITLE:$it") }
            card.mobilePhone?.let { appendLine("TEL;TYPE=CELL:$it") }
            card.officePhone?.let { appendLine("TEL;TYPE=WORK:$it") }
            card.email?.let { appendLine("EMAIL:$it") }
            card.address?.let { appendLine("ADR:;;$it") }
            card.website?.let { appendLine("URL:$it") }
            appendLine("END:VCARD")
        }
        return ResultWrapper.Success(vCard)
    }
}
