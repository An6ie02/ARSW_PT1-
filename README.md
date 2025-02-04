
### Angie Natalia Mojica
### Escuela Colombiana de Ingeniería
### Arquitecturas de Software - ARSW
## Ejercicio Fórmula BBP - Parcial Practico


**Ejercicio Fórmula BBP**

La fórmula [BBP](https://en.wikipedia.org/wiki/Bailey%E2%80%93Borwein%E2%80%93Plouffe_formula) (Bailey–Borwein–Plouffe formula) es un algoritmo que permite calcular el enésimo dígito de PI en base 16, con la particularidad de no necesitar calcular nos n-1 dígitos anteriores. Esta característica permite convertir el problema de calcular un número masivo de dígitos de PI (en base 16) a uno [vergonzosamente paralelo](https://en.wikipedia.org/wiki/Embarrassingly_parallel). En este repositorio encontrará la implementación, junto con un conjunto de pruebas. 

Para este ejercicio se quiere calcular, en el menor tiempo posible, y en una sola máquina (aprovechando las características multi-core de la mismas) al menos el primer millón de dígitos de PI (en base 16). Para esto:

A continuación se hará una descripción a detalle de lo que se ha realizado para el desarrollo de cada punto. Dentro del código hay comentarios que serán mencionados para mayor comprensión.

1. Cree una clase de tipo Thread que represente el ciclo de vida de un hilo que calcule una parte de los dígitos requeridos.\
El código inicial tiene la clase PiDigits, la cual se encarga de hacer el cálculo de los dígitos de pi: método _getDigits(..)_. Como se indica en el punto, se crea una clase de tipo Thread _DigitThread_  donde esta ahora tendrá la lógica del cálculo de los dígitos.

2. Haga que la función PiDigits.getDigits() reciba como parámetro adicional un valor N, correspondiente al número de hilos entre los que se va a paralelizar la solución. Haga que dicha función espere hasta que los N hilos terminen de resolver el problema para combinar las respuestas y entonces retornar el resultado. Para esto, puede utilizar el método Join() del API de concurrencia de Java.\
Una vez se ha hecho que la lógica del cálculo de los dígitos este en la clase Thread, el método PiDigits.getDigits() será el encargado de crear los hilos. El valor de inicio de cada hilo se explica en el código en el método mencionado.\
Una vez creados los hilos se inician y se procede a programar el código que se encargará de esperar que un hilo termine su ejecución, se agreguen los dígitos calculados a la respuesta final. Para esto se hace uso del método _join()_  este se ha modificado para el punto 3, por lo cual se muestra a continuación:
    ```java
        try {
            thread.join();
            ArrayList<Byte> byteList = new ArrayList<Byte>();
            for (byte b: thread.getDigits()) {
                byteList.add(b);
            }
            digitT.addAll(byteList);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    ```
3. Ajuste la implementación para que cada 5 segundos los hilos se detengan e impriman el número de digitos que han procesado y una vez se presione la tecla enter que los hilos continúen su proceso.\
Se hace la modificación en la clase _DigitThread_ para que el hilo se detenga cada 5 segundos, para ello se han agregado comentarios al método _run()_ y por último se ajusta el método de _getDigits(..)_ para que reanude los hilos una vez se de ENTER y espere 2 segundos para imprimir la cantidad de dígitos calculados hasta el momento: para obtener este valor se ha cambiado el tipo de lista a arrayList, para así obtener el tamaño de esta lista que nos indica la cantidad de dígitos calculas hasta el tiempo transcurrido. Se han agregado comentarios descriptivos.
