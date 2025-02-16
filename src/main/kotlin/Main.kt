package org.example

import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import kotlinx.serialization.json.Json
import kotlinx.serialization.Serializable

const val RESET = "\u001B[0m"
const val GREEN = "\u001B[32m"

@Serializable
data class Comentario(
    val id: Int,
    val postId: Int,
    val userId: Int,
    val comment: String
)


fun main() {
    //  Creamos o cliente http, un obxecto empregado para realizar solicitudes e recibir respostas dun servidor web
    val client = HttpClient.newHttpClient()

    // Creamos unha solicitude á web onde está o JSON.
    //  Con builder empregamos Builder para poder crear un obxecto complexo. Como Httprequest é un obxecto inmutable, debemos configurar os parámetros antes de crealo obxecto.
    // con .uri determinamos a URL, con .GET determinamos o método a empregar (hai máis, post, put, delete...) e con .build indicamos que se construia o obxecto
    val request= HttpRequest.newBuilder()
        .uri(URI.create("https://jsonplaceholder.org/comments"))
        .GET()
        .build()
    // Creamos unha conexión ao servidor (client.send) mandando unha solicitude (request) e obtendo unha resposta (HttpResponse)
    // e especificando que queremos tratala como texto (ofString).
    val response = client.send(request,HttpResponse.BodyHandlers.ofString())
    // .body é un método de HttpResponse que permite acceder ao corpo da resposta. Aquí, almacenamos o corpo da respota nunha variable jsonBody.
    val jsonBody = response.body()
    // Interpretamos o texto JSON para convertilo nunha lista de obxectos Comentario
    val comentarios: List<Comentario> =  Json.decodeFromString(jsonBody)

    println("Indique se quere obter os comentarios dun ${GREEN}P${RESET}ost específico ou dun ${GREEN}U${RESET}suario ${GREEN}(P/U)${RESET}:")
    val accion= readln()
    if (accion.uppercase() == "P" ){
        print("Indique o ID do post sobre o que quere recibir os comentarios. Os ID actuais son os seguintes: ")
        println(comentarios.groupBy { it.postId }.keys)
        val idpost =readln().toInt()        // Obtemos o id do post sobre o cal buscalos comentarios

        val postAgrup= comentarios.groupBy { it.postId }.filter { it.key == idpost } // *** REVISAR *** Agrupámolos e filtramos polo id do post, para obter soamente os dese post

        if (idpost !in postAgrup.keys)System.exit(0)
        println("Quere agrupalos por usuario? $GREEN(S/N)$RESET")
        if (readln().uppercase()== "S"){

        // Empregamos flatten para "saír" da estrututa de mapa, empregando unha estrutura exclusivamente de lista e agrupamos por usuario
            val agrupUsuario = postAgrup.values.flatten().groupBy { it.userId }
            println("Comentarios:\n")
            agrupUsuario.values.flatten().forEach{ println(it) } // Imprimímolos comentarios
        }
        else{
            println("Comentarios:")
            postAgrup.values.flatten().forEach{ println(it) }
        }
    }
    else{
        print("Indique o ID de usuario sobre o que desexa obter os comentarios: ")
        println(comentarios.groupBy { it.userId }.keys)
        println()
        val user = readln().toInt()   // Obtemos o ID do usuario
        val comUsuario = comentarios.groupBy { it.userId }.filter { it.key == user }// Agrupamolos comentarios por ID de usuario e filtramos para obtelos do usuario que nos interesa
        //  Obtemos os valores do mapa anteriormente creado, obtémolos en formato lista ( non en formato mapa)
        // ordeámolos por ID de post e despois por ID do comentario (Xa que non temos data, para ordealos de máis antigos a máis novos, podemos facelo por ID,
        // xa que iranse xerando dacordo vanse creando os comentarios).
        //E por último, imprimímolos
        comUsuario.values.flatten().sortedWith(compareBy<Comentario>{it.postId}.thenBy { it.id }).forEach{ println(it) }
    }
}