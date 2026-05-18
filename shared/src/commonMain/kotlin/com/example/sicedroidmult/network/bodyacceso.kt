package com.example.sicedroidmult.network

val bodyacceso =
    """
        <?xml version="1.0" encoding="utf-8"?>
        <soap:Envelope xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
          <soap:Body>
            <accesoLogin xmlns="http://tempuri.org/">
              <strMatricula>%s</strMatricula>
              <strContrasenia>%s</strContrasenia>   
              <tipoUsuario>ALUMNO</tipoUsuario>
            </accesoLogin>
          </soap:Body>
        </soap:Envelope>
    """.trimIndent()

val bodyPerfil =
    """
        <?xml version="1.0" encoding="utf-8"?>
            <soap:Envelope xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
              <soap:Body>
                <getAlumnoAcademicoWithLineamiento xmlns="http://tempuri.org/" />
              </soap:Body>
            </soap:Envelope>
    """.trimIndent()

val CalificacionFinalItem =
    """
        <?xml version="1.0" encoding="utf-8"?>
            <soap:Envelope xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
                <soap:Body>
                    <getCalifUnidadesByAlumno xmlns="http://tempuri.org/" />
                </soap:Body>
            </soap:Envelope>
    """.trimIndent()

val CalificacionesUnidadItem =
    """
        <?xml version="1.0" encoding="utf-8"?>
        <soap:Envelope xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
          <soap:Body>
            <getAllCalifFinalByAlumnos xmlns="http://tempuri.org/">
              <bytModEducativo>unsignedByte</bytModEducativo>
            </getAllCalifFinalByAlumnos>
          </soap:Body>
        </soap:Envelope>
    """.trimIndent()

val KardexItem =
    """
        <?xml version="1.0" encoding="utf-8"?>
        <soap:Envelope xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
          <soap:Body>
            <getAllKardexConPromedioByAlumno xmlns="http://tempuri.org/">
              <aluLineamiento>%d</aluLineamiento>
            </getAllKardexConPromedioByAlumno>
          </soap:Body>
        </soap:Envelope>
    """.trimIndent()

val CargaAcademicaItem =
    """
        <?xml version="1.0" encoding="utf-8"?>
        <soap:Envelope xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
          <soap:Body>
            <getCargaAcademicaByAlumno xmlns="http://tempuri.org/" />
          </soap:Body>
        </soap:Envelope>
    """.trimIndent()

/*
val bodyacceso = "<Envelope xmlns=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
        "    <Body>\n" +
        "        <accesoLogin xmlns=\"http://tempuri.org/\">\n" +
        "            <strMatricula>%S</strMatricula>\n" +
        "            <strContrasenia>%S</strContrasenia>\n" +
        "            <tipoUsuario>DOCENTE</tipoUsuario>\n" +
        "        </accesoLogin>\n" +
        "    </Body>\n" +
        "</Envelope>"
*/