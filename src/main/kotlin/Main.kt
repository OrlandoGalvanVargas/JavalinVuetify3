package mx.edu.uttt
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder
import io.javalin.http.staticfiles.Location//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
import io.javalin.vue.VueComponent

// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main() {
    var app = Javalin.create { config ->
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
            ApiBuilder.get("/", VueComponent("home-page"))
            ApiBuilder.get("/main", VueComponent("main-slots"))
            ApiBuilder.get("/popular-mice", VueComponent("popular-mice"))
            ApiBuilder.get("/gaming", VueComponent("gaming-page"))
            ApiBuilder.get("/books", VueComponent("books"))
            ApiBuilder.get("/dessert-page", VueComponent("dessert-page"))
            ApiBuilder.get("/form-page", VueComponent("form-page"))

        }
    }.start()
}