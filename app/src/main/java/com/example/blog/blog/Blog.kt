package com.example.blog.blog

class Blog () {
    var ownerId: String = ""
    var avatar: Int = 0
    var title: String = ""
    var lastMsg: String = ""
    var time: String = ""
    var unreadMsg: String = ""
    var description: String = ""
    var blogId: String = ""

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Blog

        if (ownerId != other.ownerId) return false
        if (avatar != other.avatar) return false
        if (title != other.title) return false
        if (lastMsg != other.lastMsg) return false
        if (time != other.time) return false
        if (unreadMsg != other.unreadMsg) return false
        if (description != other.description) return false

        return true
    }

    override fun hashCode(): Int {
        var result = ownerId.hashCode()
        result = 31 * result + avatar
        result = 31 * result + title.hashCode()
        result = 31 * result + lastMsg.hashCode()
        result = 31 * result + time.hashCode()
        result = 31 * result + unreadMsg.hashCode()
        result = 31 * result + description.hashCode()
        return result
    }

    override fun toString(): String {
        return this.title
    }
}