package attachments

sealed interface Attachment {
    val type: String
}

class AudioAttachment(override val type: String = "Audio", audio: Audio) : Attachment

class PhotoAttachment(override val type: String = "Photo", photo: Photo) : Attachment

class VideoAttachment(override val type: String = "Video", video: Video) : Attachment

class DocumentAttachment(override val type: String = "Document", document: Document) : Attachment

class LinkAttachment(override val type: String = "Link", link: Link) : Attachment