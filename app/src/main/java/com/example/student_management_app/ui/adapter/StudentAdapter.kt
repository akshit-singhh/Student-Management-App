package com.example.student_management_app.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.student_management_app.databinding.ItemStudentBinding
import com.example.student_management_app.data.model.Student

class StudentAdapter(
    private val students: MutableList<Student>,
    private val onEdit: (Student) -> Unit,
    private val onDelete: (Student) -> Unit
) : RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

    inner class StudentViewHolder(val binding: ItemStudentBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val binding = ItemStudentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StudentViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val student = students[position]
        holder.binding.apply {
            tvName.text = "Name: ${student.name}"
            tvClass.text = "Class: ${student.class_name}"
            tvRoll.text = "Roll No: ${student.roll_no}"

            btnEdit.setOnClickListener { onEdit(student) }
            btnDelete.setOnClickListener { onDelete(student) }
        }
    }

    override fun getItemCount() = students.size
}
