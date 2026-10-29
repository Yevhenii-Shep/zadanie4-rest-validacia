# Adresár zamestnancov – Zadanie 4 (REST API + validácia dát)

REST API nad tabuľkou `employee` (DB `employee_directory`) s validáciou vstupných dát
a jednotnou JSON odpoveďou cez `GlobalExceptionHandler`.

## Endpointy

| Metóda | URL | Úspech | Chyby |
| --- | --- | --- | --- |
| GET | `/api/employees` | 200 | – |
| GET | `/api/employees/{id}` | 200 | 404, 400 (`/abc`) |
| POST | `/api/employees` | 201 | 400 (validácia, neplatný JSON), 409 (email existuje) |
| PUT | `/api/employees/{id}` | 200 | 400, 404, 409 |
| DELETE | `/api/employees/{id}` | 200 | 404, 400 (`/abc`) |

## Jednotný formát odpovede

Každá odpoveď (úspešná aj chybová) obsahuje štyri položky:

```json
{
  "message": "Validačné chyby",
  "data": null,
  "errors": [ { "field": "email", "message": "Email nemá platný formát (napr. meno.priezvisko@firma.sk)" } ],
  "timestamp": "2026-10-29T21:17:43.762Z"
}
```

`timestamp` je typu `java.time.Instant` – čas vytvorenia odpovede na serveri, serializovaný do ISO-8601 v UTC.

## Validácia emailu

Okrem `@Email` sa používa aj `@Pattern` s vlastným regulárnym výrazom (`Employee.EMAIL_REGEX`)
podľa bežných pravidiel RFC 5321/5322:

- lokálna časť má najviac 64 znakov a môže obsahovať písmená, číslice a znaky ``!#$%&'*+/=?^_`{|}~-``
- bodka nesmie byť na začiatku, na konci ani dvakrát za sebou
- doména sa skladá z častí oddelených bodkou (každá max. 63 znakov, nezačína ani nekončí pomlčkou)
- koncová doména (TLD) má aspoň 2 písmená, takže `ddrzik@ukf` neprejde
- celý email má najviac 254 znakov

## Štruktúra projektu

```
sk.ukf.vd
├── controller  EmployeeRestController
├── dao         EmployeeRepository
├── entity      Employee (validačné anotácie)
├── exception   GlobalExceptionHandler, ObjectNotFoundException, EmailAlreadyExistsException
├── response    ResponseFactory, ResponseWrapper<T>
└── service     EmployeeService, EmployeeServiceImpl (biznis pravidlá)
```

## Spustenie

1. V XAMPP spustite MySQL s databázou `employee_directory`.
2. `./mvnw spring-boot:run`
3. Požiadavky sú pripravené v Postman kolekcii `postman/Zadanie4.postman_collection.json`.

Ak port 8080 používa Apache z XAMPP: `./mvnw spring-boot:run -Dspring-boot.run.arguments=--server.port=8081`
