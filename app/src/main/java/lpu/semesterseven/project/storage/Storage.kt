package lpu.semesterseven.project.storage

private var random_file_name_counter = 0

fun assignRandomFileName() : String = (++random_file_name_counter).toString()