# Compiladores: Práctico - Integrador

**Apellido y nombres:**  
Sofía Ruiz Rocha (Legajo: 113888)  
Ignacio Bonahora (Legajo: 113835)
Fecha: 11/11/2024

**Introducción**
En este proyecto integrador, nos propusimos desarrollar un compilador básico que abarca desde el análisis léxico hasta la generación de código. El desarrollo de este trabajo nos permitió profundizar en cada una de las etapas críticas de un compilador y comprender los desafíos y técnicas necesarias para el procesamiento de lenguajes de programación. Con una implementación en Java, hemos abordado el análisis léxico, sintáctico, semántico y la generación de código, cubriendo así los conceptos fundamentales de la asignatura de Compiladores.

**Índice**

Objetivos
Especificaciones del compilador
Implementación de los módulos
Análisis Léxico
Análisis Sintáctico
Análisis Semántico
Generación de Código
Manejo de errores
Diseño e implementación
Pruebas
Conclusiones

**Objetivos**
El objetivo de este proyecto es implementar un compilador básico en java que cumpla con las siguientes metas:

- Implementar los módulos de análisis léxico, sintáctico y semántico.
- Adicionalmente, este proyecto busca contribuir al objetivo general de la asignatura, que es construir un proyecto completo de un compilador en Java.

**Especificaciones del Compilador**
El compilador reconoce un conjunto específico de características del lenguaje, incluyendo:

_Comentarios_

/_ ... _/: Comentarios en varias líneas.
// ...: Comentarios en una línea.

_Palabras Reservadas_

- Tipo de Variables
  long
  double

- Control de Flujo
  if, then, else
  while, break

- Entrada-Salida
  read, write

- Operadores
  Aritméticos: +, -, /\*, /
  Lógicos y relacionales: >, <, >=, <=, ==, !=, <>, &&, ||

- Identificadores
  Los identificadores (nombres de variables) deben comenzar con un \_ guion bajo y seguir con letras.

**Implementación de los Módulos**

_Análisis Léxico_
Para el análisis léxico, desarrollamos el archivo Lexer.java, que se encarga de escanear el código fuente y dividirlo en tokens. Este módulo identifica palabras clave, operadores, identificadores y delimitadores, entre otros. Además, asegura que los tokens cumplan con las especificaciones del lenguaje y lanza errores léxicos cuando encuentra secuencias inválidas.

_Análisis Sintáctico_
El Parser.java se encarga de construir el árbol de sintaxis del código fuente. Este módulo utiliza las reglas gramaticales del lenguaje para estructurar el código en un árbol, facilitando la validación de la secuencia lógica de los tokens. La implementación del parser nos permitió trabajar con la estructura de las oraciones en el lenguaje y verificar la correcta construcción de las instrucciones.

_Análisis Semántico_
El archivo SemanticAnalyzer.java procesa el árbol sintáctico generado para verificar la validez semántica del programa. En esta etapa, se validan tipos de datos, la coherencia de las operaciones y se asegura el cumplimiento de las reglas semánticas del lenguaje. Este módulo ayuda a detectar errores de contexto, como operaciones entre tipos incompatibles.

_Manejo de Errores_
A lo largo de cada módulo, implementamos mecanismos de manejo de errores específicos:

- Errores Léxicos: Detección de caracteres o secuencias inválidas.
- Errores Sintácticos: Detección de estructuras incorrectas o sintaxis no reconocida.
- Errores Semánticos: Verificación de coherencia en el contexto, como tipos incompatibles en operaciones.

Cada error es documentado y lanzado con un mensaje claro, que incluye la posición del token en el listado de tokens y la descripción del problema.

**Diseño e Implementación**
La arquitectura del proyecto está dividida en módulos independientes, cada uno de los cuales representa una fase del compilador. Esta estructura modular facilita la comprensión y el mantenimiento del código. La implementación se realizó en Java, seleccionando este lenguaje por su robustez y amplia documentación, lo que nos permitió enfocarnos en los aspectos técnicos del análisis y procesamiento de código.

**Pruebas**
Para verificar la funcionalidad del compilador, realizamos pruebas exhaustivas en cada módulo:

- Pruebas Léxicas: Evaluamos distintos patrones de tokens, asegurándonos de que el Lexer reconozca correctamente cada componente del lenguaje.
- Pruebas Sintácticas: Desarrollamos casos de prueba que cubren la variedad de estructuras sintácticas del lenguaje, verificando que el parser construya correctamente el árbol sintáctico.
- Pruebas Semánticas: Se probaron casos con tipos incompatibles y operaciones inválidas para comprobar la correcta identificación de errores semánticos.

**Posibles Mejoras Futuras**

- Optimización de la Generación de Código: La actual generación de código podría ser optimizada para producir un código más eficiente en términos de rendimiento y uso de recursos. Implementar técnicas de optimización como la eliminación de código muerto o la simplificación de expresiones podría reducir la carga del intérprete.
- Mejora en el Manejo de Errores: Aunque se implementaron mensajes de error detallados, sería ideal mejorar el sistema de reportes de errores, proporcionando sugerencias para corregirlos o incluso implementando una recuperación de errores para continuar el análisis tras un fallo.
- Ampliación del Soporte de Tipos de Datos: Actualmente, el compilador soporta solo tipos básicos (long y double). Extender este soporte a otros tipos, como int, float, o incluso tipos de datos complejos, añadiría mayor versatilidad y realismo al lenguaje compilado.
- Soporte para Funciones y Procedimientos: Una funcionalidad avanzada sería permitir definiciones de funciones o procedimientos en el código fuente, lo que permitiría modularizar el código y reutilizar fragmentos de manera eficiente.
- Análisis y Optimización Semántica Avanzada: Implementar análisis más sofisticados, como la detección de variables no usadas, optimización de bucles o análisis de alcance, podría mejorar la eficiencia del código resultante y proporcionar advertencias más detalladas al programador.

**Conclusiones**

Este proyecto integrador nos permitió adentrarnos en la construcción de un compilador, comprendiendo y aplicando cada fase del análisis de código. A lo largo del desarrollo, enfrentamos desafíos en la implementación de los distintos módulos y el manejo de errores, pero logramos superarlos y consolidar una estructura funcional que cumple con las especificaciones requeridas. La implementación modular y las pruebas exhaustivas nos permitieron construir un proyecto robusto y escalable. Este trabajo refuerza nuestro conocimiento en el desarrollo de compiladores y nos brinda una base sólida para enfrentar proyectos más complejos en el futuro.
