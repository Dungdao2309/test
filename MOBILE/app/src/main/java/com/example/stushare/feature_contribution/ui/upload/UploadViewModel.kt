
package com.example.stushare.feature_contribution.ui.upload

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.stushare.feature_contribution.db.AppDatabase
import com.example.stushare.feature_contribution.db.DocumentRepository
import com.example.stushare.feature_contribution.db.NotificationEntity
import com.example.stushare.feature_contribution.db.SavedDocumentEntity
import com.example.stushare.feature_contribution.ui.noti.NotificationItem
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.example.stushare.R

class UploadViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getInstance(application)
    private val notificationDao = database.notificationDao()
    private val documentRepository = DocumentRepository(database.savedDocumentDao())

    private val _isUploading = MutableStateFlow(false)
    val isUploading = _isUploading.asStateFlow()

    private val _uploadEvent = MutableSharedFlow<UploadResult>()
    val uploadEvent = _uploadEvent.asSharedFlow()

    sealed class UploadResult {
        data class Success(val message: String) : UploadResult()
        data class Error(val message: String) : UploadResult()
    }

    fun handleUploadClick(title: String, description: String?) { // Đã khớp tham số với UploadScreen
        viewModelScope.launch {
            _isUploading.value = true
            try {
                // Tạo object tài liệu
                val newDocument = SavedDocumentEntity(
                    documentId = "",
                    title = title,
                    author = "Người dùng hiện tại",
                    subject = "Môn học chung",
                    metaInfo = "0 lượt tải · Môn học chung",
                    addedTimestamp = System.currentTimeMillis()
                )

                documentRepository.uploadDocument(newDocument)

                // Tạo thông báo
                val newNotification = NotificationEntity(
                    title = "Tải lên thành công",
                    message = "Tài liệu: $title",
                    timestamp = System.currentTimeMillis(),
                    type = NotificationItem.Type.SUCCESS.name,
                    isRead = false
                )
                notificationDao.addNotification(newNotification)

                _uploadEvent.emit(UploadResult.Success("Upload tài liệu thành công"))

            } catch (e: Exception) {
                _uploadEvent.emit(UploadResult.Error(e.message ?: "Đã xảy ra lỗi khi upload"))
            } finally {
                _isUploading.value = false
            }
        }
    }
}