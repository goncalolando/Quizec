package pt.isec.marco.firebase.ui.viewmodels

import com.google.firebase.firestore.DocumentSnapshot

data class Questionario(
    var id: String,
    var idUtilizador: String,
    val descricao: String,
    val perguntas: List<String>
){
    companion object {
        fun fromFirestore(document: DocumentSnapshot): Questionario {
            return Questionario(
                id = document.id,
                idUtilizador = document.getString("idUtilizador") ?: "",
                descricao = document.getString("descricao") ?: "",
                perguntas = document.get("perguntas") as? List<String> ?: listOf(),
            )
        }
    }
}