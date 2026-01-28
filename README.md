# AquaControl 🌊

**AquaControl** es una aplicación móvil desarrollada en **Android (Kotlin)** cuyo objetivo es registrar, monitorear y visualizar en tiempo real la **calidad del agua**, mediante el uso de sensores físicos y técnicas de minería de datos.

El proyecto fue desarrollado de manera individual con fines académicos por **Abril Reyes Romero**, egresada de Ingeniería en Software.

---

## Descripción del proyecto

AquaControl permite la captura y análisis de datos provenientes de sensores de:

- pH
- Temperatura
- Turbidez

Los datos son almacenados y procesados para su posterior análisis, permitiendo apoyar la toma de decisiones relacionadas con el control y evaluación de la calidad del agua.

---

## Tecnologías utilizadas

### Aplicación móvil
- **Android Studio**
- **Kotlin**
- **Firebase** (base de datos en tiempo real)
- **Google Maps API** (visualización geográfica de los datos)

### Minería de datos y Dashboard
- **Python**
- **Flask**
- **Dashboard web** para visualización y análisis de datos
- **ngrok** para la exposición del servidor local y su integración con la aplicación móvil

---

## Arquitectura general

1. Los sensores capturan los datos de pH, temperatura y turbidez.
2. Los datos son enviados y almacenados en **Firebase**.
3. La aplicación móvil consume los datos en tiempo real.
4. La minería de datos se procesa en un **Dashboard desarrollado con Flask**.
5. El Dashboard se integra con la aplicación móvil mediante **ngrok**, permitiendo la visualización externa del análisis de datos.

---

## Demostración del funcionamiento

Debido a que la aplicación depende de sensores físicos y servicios externos, el funcionamiento completo se muestra en el siguiente enlace:

**Carpeta de Drive (videos del sistema en funcionamiento):**  
 *( https://drive.google.com/drive/folders/11bcWx1O0K3GcpitngNxcJvBgAgwlJp-e )*

En esta carpeta se incluyen:
- Videos del funcionamiento de la aplicación móvil
- Visualización del Dashboard de minería de datos
- Integración entre la app, Firebase y Flask mediante ngrok

---

## Estado del proyecto

- Proyecto académico finalizado
- Funcional para demostraciones
- Escalable para implementación en entornos reales

---

## Autora

**Abril Reyes Romero**  
Ingeniería en Software  
Proyecto desarrollado de manera individual

---

## Nota

Este repositorio tiene fines demostrativos y forma parte del portafolio profesional de la autora.
