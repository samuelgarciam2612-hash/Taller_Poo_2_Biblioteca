Bliblioteca Universitaria
Samuel Garcia: samuelgarciam2612-hash
Camila Hernandez: mcamilahc
Nicolas Perez: Lester369
David Ceballos: dceballos12

El proyecto lo organizamos por capas para que el código quede ordenado, fácil de mantener y no mezclar responsabilidades:

* **`model`:** Aquí están las clases principales del sistema como `Usuario`, `Material`, `Prestamo` y `Multa`, usando herencia y polimorfismo para las reglas de cada tipo de material o usuario.
* **`repository`:** Maneja el guardado de datos en memoria usando genéricos con `Repositorio<T>` y `Estanteria<T>`[cite: 5].
* **`service`:** Es el cerebro del sistema (`BibliotecaService`), donde se maneja la lógica de prestar, devolver, cobrar multas y sacar reportes con Streams.
* **`pattern`, `util`, `exception`:** Contiene los patrones de diseño (Factory y Observer), las clases utilitarias, los comparadores y todas las excepciones personalizadas del sistema.

Para la regla de negocio de reservar materiales cuando ya están prestados, implementamos una lista de espera con la clase `ColaEspera<T>` aplicando el concepto FIFO (First In, First Out).

Para lograr esto usamos por debajo la interfaz `java.util.Queue` de Java[cite: 5]:
Orden de llegada justo:** Cuando un usuario intenta pedir un libro que está ocupado, entra a la cola con `encolar`. El primero que pide la reserva queda de primero para recibirlo.
Atención automática:** Apenas el material se devuelve, el sistema saca al siguiente usuario en turno con `atenderSiguiente` y se le notifica mediante el patrón Observer que su recurso ya está disponible para préstamo.

Para organizarnos en Git y trabajar de forma colaborativa sin pisarnos el código, cada uno desarrolló su módulo en una rama específica a partir de `master` y fuimos integrando todo mediante commits y merges:

* **Samuel García (rama: `feature/servicio-y-pruebas`):** Me encargué de estructurar el servicio principal (`BibliotecaService`) con toda la lógica de préstamos, devoluciones y reportes con Streams. También implementé la clase de utilidades `BibliotecaUtils`, configuré la trazabilidad del sistema con los logs de `java.util.logging` y construí la suite de pruebas unitarias en JUnit 5.

* **Camila Hernández (rama: `feature/modelo-y-excepciones`):** Estuvo a cargo del modelado del dominio base, implementando las entidades de `Usuario`, `Material`, `Prestamo` y `Multa`, junto con la jerarquía completa de excepciones personalizadas para controlar los errores de negocio.

* **David Ceballos (rama: `feature/genericos-utils`):** Desarrolló las estructuras de datos genéricas del sistema, creando la clase `Estanteria<T>` para el repositorio en memoria y `ColaEspera<T>` para la gestión FIFO de turnos, además de liderar el diseño de la arquitectura en los diagramas UML.

* **Nicolás Pérez (rama: `feature/patrones-y-comparadores`):** Implementó la creación de materiales mediante el patrón Factory Method, el sistema reactivo con Observer (`GestorUsuarios` y `UsuarioObservador`) y los comparadores de materiales (`ComparatorLC`, por año y multa) usando lambdas y comparadores compuestos.
  


Single Responsibility Principle (Principio de Responsabilidad Única): 
  Cada clase tiene un solo propósito bien definido. Por ejemplo, `Prestamo` únicamente gestiona las fechas, el estado y el cálculo de multas de una transacción; `Estanteria<T>` se limita al almacenamiento y persistencia en memoria[cite: 1]; y `BibliotecaService` se encarga de coordinar la lógica de negocio sin involucrarse en cómo se imprimen los datos ni cómo se crean internamente las entidades.

Open/Closed Principle (Principio de Abierto/Cerrado):  
  El sistema está abierto a la extensión pero cerrado a la modificación. Esto se evidencia directamente en el patrón **Factory Method**: si el día de mañana la biblioteca necesita admitir un nuevo tipo de material (como `Tesis` o `Audiolibro`), no tenemos que modificar el código existente en `MaterialFactory` ni en `BibliotecaService`; basta con crear la nueva subclase que extienda de `Material` y su respectiva fábrica.

Liskov Substitution Principle (Principio de Sustitución de Liskov):  
  Cualquier subclase puede reemplazar a su clase base sin alterar el correcto funcionamiento del programa. Las clases hijas `Libro`, `Revista`, `TrabajoDeGrado` y `RecursoDigital` pueden ser utilizadas indistintamente donde se espere una referencia a `Material`, respetando los contratos de métodos como `getDiasPrestamo()` y `getTarifaMultaDiaria()` sin romper las expectativas del servicio. Lo mismo ocurre con `Estudiante` y `Docente` al extender de `Usuario`.

Interface Segregation Principle (Principio de Segregación de Interfaces):  
  Diseñamos interfaces pequeñas y específicas en lugar de interfaces monolíticas y sobrecargadas. `Identificable` solo exige el método `getId()` para las búsquedas genéricas; `Observador` solo expone `estaDisponible()` para la recepción de eventos; y `ValidadorPrestamo` define una única función de validación para ser usada de forma limpia con expresiones lambda. Ninguna clase está forzada a implementar métodos que no necesita.

Dependency Inversion Principle (Principio de Inversión de Dependencias):  
  Los módulos de alto nivel no dependen de implementaciones concretas, sino de abstracciones e interfaces. El servicio `BibliotecaService` y los clientes operan contra contratos abstractos como `Repositorio<T>` en vez de acoplarse directamente a la implementación física, y las fábricas concretas dependen de la interfaz `MaterialFactory` para retornar el producto abstracto `Material`.
