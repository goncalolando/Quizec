package pt.isec.marco.firebase.ui.viewmodels

import com.google.firebase.firestore.DocumentSnapshot

data class Partilha(
    var id: String,
    var idUtilizador: String,
    val tempoEspera: Int,
    val tempoResolucao: Int
){

    companion object {
        fun fromFirestore(document: DocumentSnapshot): Partilha {
            return Partilha(
                id = document.id,
                idUtilizador = document.getString("idUtilizador") ?: "",
                tempoEspera = 2,
                tempoResolucao = 2
            )
        }
    }
}