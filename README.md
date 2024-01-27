# Tuplaus-peli

## Käynnistys
Käännä projekti ja käynnistä palvelin **mvn clean install spring-boot:run**

## Tietokanta
Peli käyttää muistinvaraista tietokantaa. Voit kuitenkin yhdistää siihen **jdbc:hsqldb:hsql://localhost:12345/tuplaus_db** (käyttäjätunnus **sa**, ei salasanaa).

## Dokumentaatio
API-dokumentaatio löytyy täältä [Swagger-UI](http://localhost:8080/swagger-ui/index.html)

## Pelaaminen

### 1. kierros
Pelin ensimmäisen kierroksen pelaaminen **curl -X POST -H "Content-Type: application/json" --data @play.json http://localhost:8080/game/doubling/play**

jossa _play.json_

```json
{
  "userId": 1,
  "bet": 10,
  "choice": "SMALL"
}
```

### Seuraavat kierrokset
Tietenkin vain, jos edellinen kierros päätyi voittoon **curl -X POST -H "Content-Type: application/json" --data @double.json http://localhost:8080/game/doubling/double**

jossa _double.json_

```json
{
  "play": true,
  "gameTransactionId": 1,
  "choice": "BIG"
}
```

## Testidata

Sovellukseen alustetaan käyttäjät:

![image](https://github.com/ollipuljula/tuplaus/assets/23175947/5041c84d-2007-4d68-b8d4-f3fac55bb4f9)

