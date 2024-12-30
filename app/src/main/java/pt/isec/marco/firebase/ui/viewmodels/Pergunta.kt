package pt.isec.marco.firebase.ui.viewmodels

import com.google.firebase.firestore.DocumentSnapshot

data class Pergunta(
    var id: String,
    var idUtilizador: String,
    val titulo: String,
    val imagem: String,
    val respostas: List<String>,
    val respostaCerta: List<String>,
    val tipo: String
){
    companion object {
        fun fromFirestore(document: DocumentSnapshot): Pergunta {
            return Pergunta(
                id = document.id,
                idUtilizador = document.getString("idUtilizador") ?: "",
                titulo = document.getString("titulo") ?: "",
                imagem = document.getString("imagem") ?: "",
                respostas = document.get("respostas") as? List<String> ?: listOf(),
                respostaCerta = document.get("respostaCerta") as? List<String> ?: listOf(),
                tipo = document.getString("tipo") ?: ""
            )
        }
    }
}
