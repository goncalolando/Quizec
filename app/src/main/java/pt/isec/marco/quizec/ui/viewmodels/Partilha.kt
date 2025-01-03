package pt.isec.marco.quizec.ui.viewmodels

import com.google.firebase.firestore.DocumentSnapshot

data class Partilha(
    var id: String,
    var idQuestionario: String,
    var tempoEspera: Long,
    var duracao: Long,
){
    companion object {
        fun fromFirestore(document: DocumentSnapshot): Partilha {
            return Partilha(
                id = document.getString("id") ?: "",
                idQuestionario = document.getString("idQuestionario") ?: "",
                tempoEspera = document.getLong("tempoEspera") ?: 0,
                duracao = document.getLong("duracao") ?: 0
            )
        }
    }
}
