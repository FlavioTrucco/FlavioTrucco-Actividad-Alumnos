/**
 * Esta clase UserController pertenece al paquete controllers.
 * En principio, solo debería atender a los requests HTTP,
 * y según sea GET, POST, u otro método, analizar el contenido
 * del request, y decidir a qué método llamar. 
 * En principio, esta clase no debería hacer el trabajo. No.
 * Lo que debería hacer es llamar al método encargado de hacer
 * el trabajo y pasarle los parámetros necesarios. 
 * Ese método llamado debería pertenecer a una clase del paquete services. 
 * Pero nosotros no tenemos ese paquete, porque este es un ejemplo muy simple.
  * Veremos que esta clase 'UserController' hace todo el trabajo, lo que no debería ser así.
 * Entonces, tenemos que recordar que estamos dejando de lado un principio
 * muy importante, para no complicar este ejemplo.
 */
package tup.alumnos.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import tup.alumnos.models.Alumno;
import tup.alumnos.repositories.AlumnoRepository;

/**
 * La anotación @RestController es la combinación de @Controller
 * y @ResponseBody.
 * Está anotando la clase, o sea que todos los métodos la heredan y
 * no es necesario anotar cada uno de ellos. Todos tendrán la semántica
 * de @ResponseBody. Esto significa que la String retornada es la response, no
 * el nombre de una vista.
 */
@RestController
// La URL que va entre paréntesis en esta anotación habrá que agregarla detrás
// del puerto 8080 en todas las llamadas a esta aplicación.
// Por ejemplo @RequestMapping("/user") resultaría en lo siguiente:
// localhost:8080/user.... y detrás de esto habría que agregar el
// resto de la URL.
// En este caso, no necesitamos nada, y queda simplemente localhost:8080
@RequestMapping("")
public class AlumnoController {
  /**
   * Qué es un bean en Java
   * https://stackoverflow.com/a/3295517/2740402
   * Un JavaBean es solo un estándar. Es una clase regular de Java, excepto que
   * sigue ciertas convenciones:
   * - Todas las propiedades son privadas (usan getters y setters).
   * - Tiene un constructor público sin argumentos.
   * - Implementa la interfaz Serializable.
   * Eso es todo. Es solo una convención.
   * 
    * La anotación '@Autowired' significa que Spring va a inyectar en esta clase un
   * bean llamado 'userRepository' de tipo 'UserRepository'.
   * No hay en este proyecto una clase 'UserRepository'. Solo hay una
   * interfaz 'UserRepository'. Y esta interfaz lo único que hace es extender
   * 'CrudRepository'. No declara ni campos ni métodos. Nosotros no hacemos nada,
   * todo lo hace Spring por nosotros.
   * Esta es la inyección de dependencia. Nosotros lo único que hacemos es
   * declarar la variable userRepository de tipo UserRepository, y ponerle
   * la anotación Autowired. Y listo. Ya tenemos en esta clase UserController
   * la variable userRepository correctamente configurada e inicializada, de
   * manera que la podemos usar sin más.
   *  * Notar que tampoco hemos programado los métodos que estamos llamando,
   * y que están declarados en la interfaz 'CrudRepository'.
   * Los nombres de los métodos que nosotros creamos en esta clase
   * son arbitrarios. Pero los nombres de los métodos que invocamos
   * sobre el objeto 'userRepository' tienen que ser los de la interfaz.
   */
  @Autowired
  private AlumnoRepository alumnoRepository;

  @PostMapping("/add") // Map ONLY POST Requests
    // @RequestParam means it is a parameter from the GET or POST request
  public String addNewUser(@RequestParam String nombre, @RequestParam String curso, @RequestParam String sexo) {


    Alumno user = new Alumno();
    user.setNombre(nombre);
    user.setCurso(curso);
    user.setSexo(sexo);
    alumnoRepository.save(user);
    return "Se ha agregado el nuevo alumno a la base de datos";
  }

  @PostMapping("/delete/{id}") // Map ONLY POST Requests
  public String deleteUserById(@PathVariable Long id) {
     /**
   * https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/bind/annotation/PathVariable.html
   * 
   * @PathVariable significa que el parámetro 'id' está ligado a una variable de
   *               template del URI
   */

    alumnoRepository.deleteById(id);
    return "Se ha eliminado el alumno de la base de datos";
  }

  @GetMapping("/{id}")
  public String findUserById(@PathVariable Long id) {
 // @PathVariable indica que el parámetro 'id', de tipo Long, es una
    // variable de template que viene en la URI.
    /**
     * https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/CrudRepository.html#findById-ID-
     * Optional<T> findById(ID id)
     * 
     * Dentro del estilo, el selector #users indica que el estilo
     * que estamos definiendo es para ser usado solamente en el
     * elemento del DOM que tiene id='users', o sea la tabla.
     */ 
    String resp = """
          <style>
            #users {
              font-family: Arial, Helvetica, sans-serif;
              border-collapse: collapse;
              width: 100%;
            }
            #users td, #users th {
              border: 1px solid #ddd;
              padding: 8px;
            }
            #users tr:nth-child(even){background-color: #f2f2f2;}
            #users tr:hover {background-color: #ddd;}
            #users th {
              padding-top: 12px;
              padding-bottom: 12px;
              text-align: left;
              background-color: #ffa600;
              color: white;
            }
          </style>
          <table id='users'>
            <tr>
              <th>Id</th>
              <th>nombre</th>
              <th>Curso</th>
              <th>Sexo</th>
            </tr>
        """;
    /**
     * https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/Optional.html
     * El método findById() tiene un tipo de retorno Optional.
     * Dicho brevemente, significa que el objeto retornado puede estar
     * presente, o no estar presente. O está, o no está. Solo hay dos opciones.
     * Si está, lo puedo extraer con el método get(). En este caso, el get()
     * me devuelve simplemente el objeto de tipo User, con sus campos
     * debidamente completados con los valores que JPA sacó de la tabla
     * correspondiente de la base de datos.
     */
    if (alumnoRepository.findById(id).isPresent()) {
      /**
       * No necesito el operador new. Solo lo uso cuando quiero crear una
       * instancia que todavía no existe, por eso es 'nueva'. En este caso,
       * el objeto user ya existe, y me viene retornado por el método get().
       * No necesito crearlo, solo se lo asigno a la variable.
       */
      Alumno user = alumnoRepository.findById(id).get();
      resp += "<tr>"
          + "<td>" + user.getId() + "</td>"
          + "<td>" + user.getNombre() + "</td>"
          + "<td>" + user.getCurso() + "</td>"
          + "<td>" + user.getSexo() + "</td>"
          + "</tr>";
           /*TODO  agregamos mensaje de error con css https://codepen.io/palimadra/pen/OVvbaY*/
    } else {
       resp ="""
        <div class='error'>Usted ingresó un id incorrecto o inexistente, por favor intente nuevamente</div>
      <style>
      .error{
          border: 1px solid;
          margin: 10px 0px;
          padding: 15px 10px 15px 50px;
          background-repeat: no-repeat;
          background-position: 10px center;
        } 
        .error{
          color: #9F6000;
			background-color: #FEEFB3;
			background-image: url('https://i.imgur.com/Z8q7ww7.png');
        }
      </style>
     
      """;

    }
    return resp + "</table>";
  }


  @GetMapping("/all")
  public String getAllUsers() {
    // This returns a JSON or XML with the users
    Iterable<Alumno> iterable = alumnoRepository.findAll();
    /**
     * Lo que viene a continuación se llama text block, 
     * y es tipo String. El Manual de Java los describe en 
     * la sección 3.10.6 Text Blocks.
     * 
     * La variable resp es de tipo String, y le vamos a asignar un bloque de texto.
     * Ese bloque de texto es todo que lo que está contenido entre los dos
     * delimitadores: el de apertura y el de cierre.
     * El delimitador de apertura es la triple comilla """ que está a la
     * derecha del igual.
     * El delimitador de cierre es la triple comilla """ que está al final.
     * Todo es seguido por el punto y coma, porque es el final de una sentencia.
     * 
     * No es buen estilo incluir cadenas largas en un archivo de código fuente.
     * Esto lo hago solo para no introducir una complicación que no agregaría
     * nada a los conceptos que estoy discutiendo ahora.
     * 
     * Comenzamos por poner unos estilos CSS, para que la tabla quede más linda.
     * 
     * Cuando terminamos con los estilos, arrancamos con el HTML de la
     * tabla misma. Lo primero que hacemos es generar una fila y en las
     * celdas de esa fila poner los encabezados, que son los nombres de
     * las columnas o campos de la tabla que está en la base de datos.
     */
    String resp = """
          <style>
            #users {"
              font-family: Arial, Helvetica, sans-serif;
              border-collapse: collapse;
              width: 100%;
            }
            #users td, #users th {
              border: 3px solid #ddd;
              padding: 8px;
            }
            #users tr:nth-child(even){background-color: #f2f2f2;}
            #users tr:hover {background-color: #ddd;}
            #users th {
              padding-top: 12px;
              padding-bottom: 12px;
              text-align: left;
              background-color: #ffa600;
              color: white;
            }
          </style>
          <table id ='users'>
            <tr>
              <th>Id</th>
              <th>nombre</th>
              <th>curso</th>
              <th>sexo</th>
            </tr>
        """;
        /**
         * Ya terminé con la fila de los encabezados, y ahora tengo que
         * generar el cuerpo de la tabla, una fila por cada registro.
         * No puedo usar forEach() con una función lambda
         * porque el scope de las variables no lo permite.
         * Por eso uso el for mejorado, para recorrer el objeto iterable.
         */
    for (Alumno user : iterable) {
      resp += "<tr>"
          + "<td>" + user.getId() + "</td>"
          + "<td>" + user.getNombre() + "</td>"
          + "<td>" + user.getCurso() + "</td>"
          + "<td>" + user.getSexo() + "</td>"
          + "</tr>";
    }
    return resp + "</table>";
  }

  @GetMapping("")
  public String hola() {
   
   
    String resp = """
      <h1 class='Bienvenidos'>Hola bienvenidos al registro de datos de la escuela "Santa María"</h1>
    <style>

  .Bienvenidos{
    background-color: #f2f2f2;
    border: 1px solid #ddd;
    padding:12px;
    padding-left: 8px;
    font-style:italic;
    width: 70%;
    text-align:center;
  }

    </style>
   
    """;
    return resp;
      }
  }