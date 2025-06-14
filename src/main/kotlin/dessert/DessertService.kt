package mx.edu.uttt.dessert

import io.javalin.http.InternalServerErrorResponse
import kotliquery.HikariCP
import kotliquery.Row
import kotliquery.queryOf
import kotliquery.sessionOf

data class Dessert(
    var id: String,
    val name: String,
    val calories: Int,
    val fat: Double,
    val carbs: Int,
    val protein : Double
)
object DessertService {

    private fun rowToDessert(row: Row) = Dessert(
        id = row.string("ID"),
        name = row.string("DNAME"),
        calories = row.int("CALORIES"),
        fat = row.double("FAT"),
        carbs = row.int("CARBS"),
        protein = row.double("PROTEIN")
    )

    fun selectAll(): List<Dessert> {
        val qry = queryOf("""
            SELECT UUID_TO_CHAR(a.ID) ID, a.DNAME, a.CALORIES, a.FAT, a.CARBS, a.PROTEIN
FROM DESSERTS a""".trimIndent())
            .map(::rowToDessert).asList
        val result: List<Dessert>

        sessionOf(HikariCP.dataSource()).use {conn ->
            result = conn.run(qry)
        }
        return result
    }

    fun selectById(id: String): Dessert {
        val qry = queryOf("""
            SELECT UUID_TO_CHAR(a.ID) ID, a.DNAME, a.CALORIES, a.FAT, a.CARBS, a.PROTEIN
FROM DESSERTS a
WHERE a.ID = char_to_uuid(?)""".trimIndent(), id)
            .map(::rowToDessert).asSingle
        val result: Dessert
        sessionOf(HikariCP.dataSource()).use {conn ->
            result = conn.run(qry) ?: throw InternalServerErrorResponse("No existe ese elemento en la base de datos");
        }
        return result
    }

    fun insert(dessert: Dessert): String {
        val qry = queryOf("""
            INSERT INTO DESSERTS (ID, DNAME, CALORIES, FAT, CARBS, PROTEIN)
            VALUES (CHAR_TO_UUID(?),?, ?, ?, ?,?)""".trimIndent(), dessert.id, dessert.name, dessert.calories, dessert.fat,
            dessert.carbs, dessert.protein)
            var result = "failed"
            sessionOf(HikariCP.dataSource()).use {conn ->
                result = if (conn.run(qry.asUpdate) > 0) dessert.id else throw InternalServerErrorResponse("No se pudo insertar");
            }
        return result
    }

    fun delete(id: String): String {
        val qry = queryOf("""
            DELETE FROM DESSERTS a 
            WHERE a.ID = CHAR_TO_UUID(?)""".trimIndent(), id)
        var result = "Failed"
        sessionOf(HikariCP.dataSource()).use {conn ->
            result = if (conn.run(qry.asUpdate) > 0) "success" else throw InternalServerErrorResponse("No se pudo borrar");
        }
        return result

    }

    fun update(dessert: Dessert): String {
        val qry = queryOf("""
            UPDATE DESSERTS a
SET 
    a.DNAME = ?, 
    a.CALORIES = ?, 
    a.FAT = ?, 
    a.CARBS = ?, 
    a.PROTEIN = ?
WHERE
    a.ID = CHAR_TO_UUID(?)
        """.trimIndent(), dessert.name, dessert.calories, dessert.fat, dessert.carbs, dessert.protein,
            dessert.id)
        var result = "failed"
        sessionOf(HikariCP.dataSource()).use {conn ->
            result = if (conn.run(qry.asUpdate) > 0) "success" else throw InternalServerErrorResponse("No se pudo actualizar");
        }
        return result
    }
}