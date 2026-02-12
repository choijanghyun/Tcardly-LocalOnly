package com.tcardly.data.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tcardly.core.common.model.ResultWrapper
import com.tcardly.core.database.dao.CompanyInfoDao
import com.tcardly.core.database.entity.CompanyInfoEntity
import com.tcardly.domain.model.AiAnalysisReport
import com.tcardly.domain.model.CompanyInfo
import com.tcardly.domain.model.FinancialYear
import com.tcardly.domain.repository.CompanyRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CompanyRepositoryImpl @Inject constructor(
    private val dao: CompanyInfoDao,
    private val gson: Gson
) : CompanyRepository {

    companion object {
        private const val COMPANY_CACHE_TTL = 7L * 24 * 60 * 60 * 1000  // 7일
        private const val AI_CACHE_TTL = 24L * 60 * 60 * 1000            // 24시간
    }

    override suspend fun getCompanyInfo(companyName: String): ResultWrapper<CompanyInfo> {
        return try {
            // 캐시 확인 (TTL 7일)
            val cached = dao.getByName(companyName)
            if (cached != null && System.currentTimeMillis() - cached.fetchedAt < COMPANY_CACHE_TTL) {
                return ResultWrapper.Success(cached.toDomain())
            }

            // TODO: DART OpenAPI 호출 → CompanyInfoEntity 저장
            // 현재는 캐시가 있으면 반환, 없으면 에러
            if (cached != null) {
                ResultWrapper.Success(cached.toDomain())
            } else {
                ResultWrapper.Error("기업 정보를 찾을 수 없습니다. 인터넷 연결을 확인해 주세요.")
            }
        } catch (e: Exception) {
            ResultWrapper.Error("기업 정보 조회 중 오류가 발생했습니다.", e)
        }
    }

    override suspend fun requestAiAnalysis(companyName: String): ResultWrapper<AiAnalysisReport> {
        return try {
            // 캐시 확인 (TTL 24시간)
            val cached = getAiAnalysisCache(companyName)
            if (cached != null && System.currentTimeMillis() - cached.generatedAt < AI_CACHE_TTL) {
                return ResultWrapper.Success(cached)
            }

            // TODO: Gemini API 호출 → AiAnalysisCacheEntity 저장
            ResultWrapper.Error("AI 분석을 위해 인터넷 연결이 필요합니다.")
        } catch (e: Exception) {
            ResultWrapper.Error("AI 분석 중 오류가 발생했습니다.", e)
        }
    }

    override suspend fun getAiAnalysisCache(companyName: String): AiAnalysisReport? {
        val entity = dao.getAiCache(companyName) ?: return null
        return try {
            gson.fromJson(entity.analysisJson, AiAnalysisReport::class.java)
        } catch (_: Exception) {
            null
        }
    }

    override suspend fun sendAiQuestion(
        companyName: String,
        question: String,
        context: List<String>
    ): ResultWrapper<String> {
        return try {
            // TODO: Gemini API 호출 (Q&A 모드)
            ResultWrapper.Error("AI Q&A를 위해 인터넷 연결이 필요합니다.")
        } catch (e: Exception) {
            ResultWrapper.Error("AI 질문 처리 중 오류가 발생했습니다.", e)
        }
    }

    private fun CompanyInfoEntity.toDomain(): CompanyInfo {
        val financialData: List<FinancialYear> = try {
            if (financialDataJson.isNullOrBlank()) emptyList()
            else gson.fromJson(financialDataJson, object : TypeToken<List<FinancialYear>>() {}.type)
        } catch (_: Exception) { emptyList() }

        return CompanyInfo(
            id = id, companyName = companyName, ceoName = ceoName,
            industry = industry, foundedDate = foundedDate, address = address,
            employeeCount = employeeCount, financialData = financialData,
            creditGrade = creditGrade, isListed = isListed, website = website,
            fetchedAt = fetchedAt
        )
    }
}
