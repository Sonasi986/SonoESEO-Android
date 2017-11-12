# SonoESEO - Application de gestion interne [Android]

[![Version](https://img.shields.io/badge/Version-2.5-green.svg)](https://play.google.com/store/apps/details?id=fr.sonoeseo.sonoapp&hl=fr)
![Code](https://img.shields.io/badge/Code-Android-orange.svg)
[![Platform](https://img.shields.io/badge/Platform-Android-red.svg)](https://developer.android.com/index.html)
[![Contributors](https://img.shields.io/badge/Contributors-Sonasi%20Katoa-blue.svg)](http://linkedin.com/in/sonasikatoa/)
[![Licence](https://img.shields.io/badge/Licence-GNU%20GPLv3-lightgrey.svg)](http://www.gnu.org/licenses/)

## SonoESEO

> [Télécharger l'application](https://play.google.com/store/apps/details?id=fr.sonoeseo.sonoapp&hl=fr) &nbsp;&nbsp; - &nbsp;&nbsp;
> [http://sonoeseo.com](http://www.sonoeseo.com)
>
> La SONO ESEO est la plus connue des associations étudiantes de sonorisation d'Angers.
>
> Elle est composée d’une vingtaine d’étudiants, spécialisée dans la sonorisation, l’éclairage
> et l’animation d’évènements musicaux:
>
> - Soirées étudiantes
> - Concerts
> - Galas
> - Spectacles
>
> Plus de 100 prestations sont réalisées chaque année (Soirées étu, concerts, galas,...)
>
> Le club loue l'ensemble de son parc son et lumière à tous les particuliers, associations ou
> entreprises à des prix très attractifs!
>
> Si vous désirez des conseils, ou si vous avez simplement une question sur notre activité et
> le monde du spectacle, n’hésitez pas à nous contacter.

## Description de l'application

> Bienvenue sur l'application officielle de la Sono ESEO.
> Celle-ci vise à faciliter la vie interne du club et son usage est donc réservé à un membre du club.
>
>
> Voici la liste des fonctionnalités :
>
> - Accueil : Liste des actualités internes - C’est ici que vous pourrez lire l'ensemble des actualités du club
> ainsi que les notifications que vous avez reçues.
>
> - Prestation : La liste des prestations du club.
> Pour indiquer votre disponibilité pour une prestation, il vous suffit de taper dessus et d'indiquer votre réponse.
>
> - Calendrier : Sous cette vue, vous pouvez visionner la liste des évènements relatifs à une date. Simplement en
> sélectionnant celle-ci.
>
> - Rappels : La liste de tous les évènements dans le club. Les prestations, les locations et les réunions.
> Un bouton est disponible afin de trier les évènements.
>
> Pour en savoir plus, un tap sur l’item de votre choix et vous pourrez avoir les détails concernant le devis,
> l’équipe de prestation mais aussi le matériel (Utile pour les locations et les prestations pour connaitre la
> liste du matériel prévu).
>
> Tips : Au tap sur le matériel, celui-ci passe du blanc au vert, au second tap du vert au rouge et au troisième
> tap du rouge au blanc.
> Utile pour suivre au fur et à mesure une location ou une prestation.
>
> - Paramètres : Rien de plus que des liens rapides vers l'outil de gestion interne, la page facebook de la sono ou
> encore le site vitrine. Mais également les moyens de contacter le développeur pour reporter un bug ou une idée
> d’amélioration.
>
> - Profil : Votre profil, votre avatar et votre équipe.
>
> - Annuaire : Afin de retrouver plus simplement nos coéquipiers, l'annuaire permet de trouver un membre du club
> et de pouvoir le contacter simplement.
>
>
> (Crédit photo : Benoît.D, Louis.P, Anthony.M - API : Sonasi.K)
>
>
> Créée par un SheepDev pour SonoESEO

## API

L'application nécessite une API pour fonctionner. En l'occurrence l'application utilise une API
rédigée par mes soins sur le serveur de la Sono ESEO.

## Philosophie de conception

### Constantes

L'ensemble des chaines de caractères sont stockées dans différents fichiers tels que <i>APIConstants et le fichier String.xml dans les ressources de l'application</i>.
S'il y a une chaine de caractères à changer, il n'y aura alors qu'à éditer l'un des fichiers précédemment cités.

### Localisation

Pour supporter la localisation, il suffit simplement de modifier le fichier String.xml dans les ressources de l'application en ajouter les langues souhaitées.

### API

L'appel des API se fait à la création d'un fragment. Il y a alors un appel vers l'API afin de récupérer les données depuis le serveur.
Cependant tous les fragments ont une variable <i>public static</i> qui est modifiée et qui permet de bloquer l'appel vers cette API (Activités) qu'une seule fois par lancement d'application. Cela nous permet de limiter l'utilisation du réseau de l'utilisateur. (Moyenne des données des activitées : 56Kb)

### Mémoire

Plusieurs fichiers sont utilisés pour stocker les données de l'application. On y stocke en l'ensemble de la réponse donnée par l'API.
Ainsi pour récupérer les données nous n'avons qu'à récupérer les données de la clé au format String, puis de parser le JSON.

### Content

L'ensemble des données utilisées par l'application sont stockées dans le fichier Content.java.

## Contact

[Sonasi.K](https://sonasi.fr) &nbsp;&nbsp; - &nbsp;&nbsp;
[![Follow me on Twitter](https://img.shields.io/twitter/follow/Sonasi986.svg?style=social&label=Follow)](https://twitter.com/Sonasi986)

## Licence

> This program is free software: you can redistribute it and/or modify it under the
> terms of the GNU General Public License as published by the Free Software Foundation,
> either version 3 of the License, or (at your option) any later version.
>
> This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
> without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
> See the GNU General Public License for more details.
>
> You should have received a copy of the GNU General Public License along with this program.
> If not, see http://www.gnu.org/licenses/.
