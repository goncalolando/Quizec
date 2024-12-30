package pt.isec.marco.firebase.ui.viewmodels

import com.google.firebase.firestore.DocumentSnapshot

data class Questionario(
    var id: String,
    var idUtilizador: String,
    val descricao: String,
    val perguntas: List<Pergunta>
){
    companion object {
        fun fromFirestore(document: DocumentSnapshot): Questionario {
            val perguntas = document.get("perguntas") as? List<Map<String, Any>> ?: listOf()

            val listaPerguntas = perguntas.mapNotNull { pergunta ->
                val idUtilizador = pergunta["idUtilizador"] as? String
                val titulo = pergunta["titulo"] as? String
                val imagem = pergunta["imagem"] as? String
                val respostas = pergunta["respostas"] as? List<String>
                val respostaCerta = pergunta["respostaCerta"] as? List<String>
                val tipo = pergunta["tipo"] as? String

                if (idUtilizador != null && titulo != null && imagem != null && respostas != null && respostaCerta != null && tipo != null) {
                    Pergunta(
                        id = document.id,
                        idUtilizador = idUtilizador ,
                        titulo = titulo,
                        imagem = imagem,
                        respostas = respostas,
                        respostaCerta = respostaCerta,
                        tipo = tipo
                    )
                } else {
                    null
                }
            }
            return Questionario(
                id = document.id,
                idUtilizador = document.getString("idUtilizador") ?: "",
                descricao = document.getString("descricao") ?: "",
                perguntas = listaPerguntas
            )
        }
    }
}