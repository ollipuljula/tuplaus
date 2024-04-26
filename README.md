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

## Tehtävänanto

Toteuta yksinkertainen pelimoottori, jonka avulla voi pelata pokeripeleistä tuttua Tuplausta. Tuplauksessa pelaaja yrittää arvata onko edessä oleva kortti pieni vai suuri. Pieni on numerot 1-6, suuri 8-13. Jos tulee 7, pelaaja häviää aina.

Pelimoottorilla tarkoitetaan tässä palvelinta, joka tarjoaa HTTP API:n peli-clienteille ja pyörittää pelilogiikkaa. Peli-clienttia ei toteuteta tässä yhteydessä.

Tehtävät:

1. Suunnittele ja dokumentoi peli-clientin ja pelimoottorin välinen HTTP-rajapinta

Pelattaessa pelikierrosta peli-client välittää pelimoottorille pelaajan yksilöivän tunnisteen, panoksen ja pelaajan valinnan (pieni tai suuri). Vastauksessa pelimoottori välittää peli-clientille arvotun kortin, tiedon siitä voittiko pelikierros, mahdollisen voiton suuruuden ja pelaajan pelitilin jäljellä olevan saldon. Voittaessaan pelaaja voi jatkaa tuplausta tai kotiuttaa voitot.

2. Suunnittele ja toteuta tietokanta pelimoottorille

Pelaajista vähintään tallennettavat tiedot:
- Palaajan yksilöivä tunniste
- Nimi
- Pelitilin saldo

Pelitapahtumasta vähintään tallennettavat tiedot:
- Aikaleima
- Pelaajan yksilöivä tunniste
- Panos
- Pelaajan valinta (pieni vai suuri)
- Arvottu kortti
- Mahdollisen voiton suuruus

Tarvittaessa lisää oman toteutuksen tarvitsemia tallennettavia tietoja.

3. Toteuta pelimoottori

Käsitellessään pelikierrosta pelimoottori arpoo pelin lopputuloksen ja vähentää pelitilin saldosta pelaajan panostuksen. Jos pelaaja voittaa, pelaaja voi joko jatkaa tuplausta tai kotiuttaa voitot. Jos pelaaja kotiuttaa voitot, pelimoottori lisää ne pelitilin saldoon.

Jos pelitilin saldo ei riitä, järjestelmä palauttaa peli-clientille virheen.

4. Tee järjestelmälle testit

5. Kirjoita ohjeet pelimoottorin ja testien ajamiseen

