# Guia de Contribució

## Estratègia de Branques

Aquest projecte segueix una estratègia simplificada basada en Git Flow, adaptada a desenvolupament individual.

### Branca `main`
- Conté el codi estable i llest per producció.
- Només s’hi fusiona codi validat i funcional.
- Ha d’estar sempre en estat compilable.

### Branques de funcionalitat
- Convenció de nom: `feature/<descripcio-curta>`
- Es creen a partir de `main`.
- Cada nova funcionalitat o millora es desenvolupa en una branca independent.
- Un cop finalitzada, es fusiona a `main` mitjançant Pull Request.

### Branques de correcció
- Convenció de nom: `fix/<descripcio-curta>`
- Es creen a partir de `main`.
- Es fusionen a `main` un cop resolta la incidència.
