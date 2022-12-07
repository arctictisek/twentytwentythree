package twentytwentytwo.day7

data class Directory(
    val parent: Directory?,
    val name: String,
    val files: MutableCollection<File>,
    val children: MutableCollection<Directory>,
    var dirFilesSize: Int = 0,
    var totalSize: Int = 0
) {
    fun size(): Int {
        val localSize = files.sumOf { it.size }
        dirFilesSize = localSize
        val childrenSize = children.sumOf { it.size() }
        totalSize = localSize + childrenSize
        return localSize + childrenSize
    }

}
data class File(val name: String, val size: Int)
