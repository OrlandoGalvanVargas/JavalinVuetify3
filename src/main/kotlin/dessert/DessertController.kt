package mx.edu.uttt.dessert

import io.javalin.apibuilder.CrudHandler
import io.javalin.http.Context
import io.javalin.http.bodyValidator
import java.util.UUID
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletableFuture.supplyAsync

object DessertController : CrudHandler {
    override fun create(ctx: Context) {
        ctx.bodyValidator<Dessert>().get().apply {
            id = UUID.randomUUID().toString().uppercase()
        }.also { dessert ->
            ctx.future { supplyAsync { DessertService.insert(dessert) }.thenAccept (ctx::result)
            }
        }
    }

    override fun getAll(ctx: Context) {
        ctx.future {
            supplyAsync { DessertService.selectAll() }.thenAccept (ctx::json)
        }
    }

    override fun getOne(ctx: Context, resourceId: String) {
        ctx.future {
            supplyAsync { DessertService.selectById(resourceId) }.thenAccept (ctx::json)
        }
    }

    override fun update(ctx: Context, resourceId: String) {
        ctx.bodyValidator<Dessert>().get().also { dessert ->
            ctx.future {
                supplyAsync { DessertService.update(dessert)}.thenAccept (ctx::result)
            }
        }
    }

    override fun delete(ctx: Context, resourceId: String) {
        ctx.future {
            supplyAsync { DessertService.delete(resourceId) }.thenAccept ( ctx::result)
        }
    }

}