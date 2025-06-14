package mx.edu.uttt
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.get
import io.javalin.apibuilder.ApiBuilder.crud
import io.javalin.apibuilder.ApiBuilder.path
import io.javalin.http.staticfiles.Location//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
import io.javalin.validation.ValidationException
import io.javalin.vue.VueComponent
import kotliquery.HikariCP
import mx.edu.uttt.dessert.Dessert
import mx.edu.uttt.dessert.DessertController
import mx.edu.uttt.dessert.DessertService
import java.util.UUID
import java.util.UUID.randomUUID

// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main() {
    HikariCP.default(
        "jdbc:firebird://localhost:3051/dessert?encoding=UTF8",
        "sysdba",
        "Darmixista5"
    )
    //println(DessertService.selectAll())
    //println(DessertService.selectById(("81E71881-9625-462E-8890-F9DEA7F0581F")))
    //println(DessertService.delete("3B6AFBEC-535E-4F82-B6D6-AF1E00EA552F"))
    //println(DessertService.selectAll())
    /*println(DessertService.insert(Dessert(
            id = UUID.randomUUID().toString().uppercase(),
            name = "Flan",
            calories = 1,
            fat = 2.0,
            carbs = 4,
            proteins = 4.2
        )
    ))*/

    val app = Javalin.create { config ->
        config.staticFiles.apply {
            enableWebjars()
            add("public", Location.CLASSPATH)
        }
        config.vue.apply {
            vueInstanceNameInJs = "app"
            rootDirectory("/vue", Location.CLASSPATH)
        }

        config.router.mount {
        }.apiBuilder {
            get("/", VueComponent("home-page"))
            get("/main", VueComponent("main-slots"))
            get("/popular-mice", VueComponent("popular-mice"))
            get("/gaming", VueComponent("gaming-page"))
            get("/books", VueComponent("books"))
            get("/dessert", VueComponent("dessert-page"))
            get("/form-page", VueComponent("form-page"))
            path("api") { // RestFull/Endpoints
                crud("desserts/{id}", DessertController)
            }
        }
    }.start()

    app.exception(ValidationException::class.java) { e, ctx ->
        val err = e.errors.values.single().joinToString { it.message }
        ctx.result(err).status(500)
    }
}