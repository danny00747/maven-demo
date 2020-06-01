---
title: Préparation examen de réseau 2ème Q2
subtitle: Notes sur le Twitch de David Aires
author: Melvin Campos Casares
date: 9 juin 2019
titlepage: true
titlepage-rule-color: "049FD9"
footer-left: Twitch de David Aires
titlepage-rule-height: 6
toc: true
toc-own-page: true
toc-title: Préapration examen de réseau 2ème Q2 - Notes du Twitch de David Aires
---

# Post Facebook

Bonjour tout le monde, comme promis petit live twitch à 14h.

Je vais tenter d’en sauver quelques un!
<https://l.facebook.com/l.php?u=https%3A%2F%2Fm.twitch.tv%2Fajaxo_%2Fprofile%3Fdesktop-redirect%3Doffline_channel%26fbclid%3DIwAR0IQzNfSNnbXkB8VPZ1oz9CNuU3fJjokrHEVEORCCb7HkwknidhR3AZVXc&h=AT2HyI7PHsDdWkjK7cdYgmI0IE3bthX8Iuvp_Ms_BBczqlyNACJgpep-A69YCQjS63NduGCgtQa6W1MhHWQfNTmaOh5SyR-l1VN7UoB8hQKnX1C5oqXxHmJfPHnfO6-7Z-Z2wtEwO8m34hwaLpPMfscFpnRNmL4Ry1REIQ>

Quelques règles (pas envie de perdre temps):

- au premier manque de sérieux sur le chat, je mets fin au live,
- je répondrai pas au question théorique (allez étudier votre cours bande de fainéant),
- pour poser une question sur le chat #question « votre question »,
- ne comparer pas mes configs aux fichiers de config qui tournent sur le groupe (incomplète),
- je ne serai pas responsable de votre échec à l’examen,
- live maximum 2h,
- il y aura un replay disponible à la fin du live.

# Notes sur la prépa d'examen de juin 2019

## S1-CSW-01

en > conf t > host S1-CSW-01

Il s'agit d'un switch layer 3 et on a besoin du routage : `ip routing`.
Ensuite `int vlan 1`, `no ip add`, `shut`, `exit`.
Idem pour `vlan 20` sauf qu'il faut ajouter `name natif-IT` et `vlan 30` avec `name SERVER`.

Dans `int vlan 20`, il faut `ip add 10.10.20.251 255.255.255.0`, `no shut`, `exit`.

Dans `int vlan 30`, `ip add 10.10.30.251 255.255.255.0`, etc.

Dans le cas où il faut annuler une commande faite, simplement `no` suivi de la commande même si de base, dans le cas des adresses IP, la nouvelle écrase d'office l'ancienne.

`int range G1/0/2-3` suivi de `channel-control lacp`.
En fonction des switch utilisé (les nouveaux), ce sera `channel-protocol lacp` pour activer l'Ether-Channel.
Ensuite `channel-group 1 mode active` puis `exit`.

`line con 0` puis `logg synchronous` pour éviter d'avoir tout le temps des messages ennuyant lorsque les ports s'activent.

En switch layer 3, certaines commandes sont pas les mêmes, attention.

`int port-channel 1`, `switchport trunk encapsulation dot1q`, `switchport mode trunk`, `switchport trunk native vlan 20`, `exit`.

`vtp domain S1.Formation.local`, `vtp password CISCO`, `service password-encryption`, `port-channel load-balance src-ip`.

`int g1/0/24`, `no switchport`, `ip add 10.10.0.2 255.255.255.248`, `no shut`, `exit`.

`spanning-tree vlan 1,20,30`, `router ospf 1`, `network 10.10.20.0 0.0.0.255 area 0` (uniquement les réseaux directement connecté, le masque de sous-réseau est en wildcard), `network 10.10.30.0 0.0.0.255 area 0`, `network 10.10.0.0 0.0.0.7 area 0`, `auto-?`, `exit`.

`int vlan 20`, `standby 20 ip 10.10.20.254`, `standby 20 priority 105` (105 aura priorité sur le 100), `standby 20 preempt`, `standby 20 track G1/0/24`.
Idem avec le 30 : `int vlan 30`, `standby 30 ip 10.10.30.254`, `standby 30 priority 105`, `standby 30 preempt`, `standby 30 track G1/0/24`.

`int vlan 20`, `ip helper 10.10.30.11`, `exit` puis `int vlan 30`, `ip helper 10.10.30.11`, `exit`.

## S1-CSW-02

en > conf t > host S1-CSW-02

`vtp domain S1.FOrmation.local`, `vtp password CISCO`, `service password-encryption`.

`vlan 20`, `name natif-IT`, `exit`, `vlan 30`, `name SERVER`, `exit`, `int vlan 1`, `no ip add`, `shut`, `exit`.

`conf t`, `int G1/0/24`, `no switchport`, `ip add 10.10.0.3 255.255.255.248`, `no shut`.

`ip routing`, `router ospf 1`, `network 10.10.20.0 0.0.0.255 area 0`, `network 10.10.30.0 0.0.0.255 area 0`, `network 10.10.0.0 0.0.0.7 area 0`, `exit`.

`line con 0`, `logg synchronous`, `exit`, `spanning-tree vlan 1, 20, 30`.

`port-channel load-balance src-ip`, `int vlan 20`, `ip add 10.10.20.252 255.255.255.0`, `standby 20 ip 10.10.20.254`, `standby 20 priority 100`, `standby 20 preempt`, `standby 20 track G1/0/24`, `ip helper 10.10.30.11`, `no shut`, `exit`.

`int vlan 30`, `ip add 10.10.30.252 255.255.255.0`, `standby 30 ip 10.10.30.254`, `standby 30 priority 100`, `standby 30 preempt`, `standby 30 track G1/0/24`, `ip helper 10.10.30.11`, `no shut`, `exit`.

`int port-channel 1`, `switchport trunk encapsulation dot1q`, `switchport mode trunk`, `switchport trunk native vlan 20`, `no shut`, `exit`.

## S1-SW-03

...

`spanning-tree vlan 1,20,30`, `host S1-SW-03`, `vtp password CISCO`, `service password-encryption`, `int range f0/23-24`, `channel-protocol lacp`, `channel-group 1 mode passive`, `exit`.

`ip domain-name S1.Formation.local`.

## S1-CSW-01

`en`, `conf t`, `ip domain-name S1.Formation.local`.

## S1-CSW-02

`en`, `conf t`, `ip domain-name S1.Formation.local`.

## S1-SW-03

`conf t`, `port-channel 1`, `port-channel load-balance src-ip`, `ip default-gateway 10.10.20.254`, `spanning-tree vlan 1,20,30`, `int f0/1`, `switchport mode access`, `switchport access vlan 30`, `exit`.

`vlan 20`, `name natif-IT`, `exit`, `vlan 30`, `name SERVER`, `exit`, `int f0/1`, `switchport access vlan 30`, `exit`, `vtp mode client`.
Ici, CSW-02 et CSW-01 seront en vtp mode server puisqu'au-dessus du SW-03.

`int vlan 20`, `ip add 10.10.20.243 255.255.255.0`.

## S1-SW-01

`host S1-SW-01`, `ip domain-name S1.Formation.local`, `vtp domain S1.Formation.local` (on voit que la config s'est déjà transmise), `vtp password CISCO`, `port-channel load-balance src-ip`, `vtp mode client` (juste pour être sûr, on ne sait jamais que ça ne se transmette pas), `int range f0/23-24`, `channel-protocol lacp`, `channel-group 1 mode passive`, `exit`.

`vtp mode server` (parce que la configuration des vlan n'est pas possible en mode client), `vlan 20`, `name natif-IT`, `exit`, `vlan 30`, `name SERVER`, `exit`.
`int f0/1`, `switchport access vlan 20`, `no shut`, `exit`.
`spanning-tree vlan 1,20,30`, `ip default-gateway 10.10.20.254`, `int vlan 20`, `ip add 10.10.20.240 255.255.255.0`, `exit`.
`int port-channel 1`, `switchport mode trunk`, `switchport trunk native vlan 20`, `no shut`, `vtp mode client`.

Si on veut gagner des points en + : `int f0/1`, `spanning-tree portfast` (ce port ne transmettra pas des informations spanning-tree sur les interfaces directement connectées (ici, PC)).

## S1-RT-O1

en > conf t > host S1-RT-01

`ip domain name S1.Formation.local`, `int g0/0/0`, `ip add dhcp`, `no shut`, `exit`.

`int g0/0`, `ip add 10.10.0.1 255.255.255.248`, `exit`.

`router ospf 1`, `network 10.10.0.0 0.0.0.7 area 0`, `default-information originate`, `exit`.
OSPF n'a pas de summary et SURTOUT NE PAS METTRE LE NETWORK INTERNET SINON NOTRE RESEAU EST ACCESSIBLE PAR N'IMPORTE QUI SUR SIMPLE REQUETE !

`ip access-list 2`, `ip access-list extended`, `ip access-list standard 2`, `permit 10.10.20.0 0.0.0.255` (attention, bien en wildcard pour que tout le sous-réseau soit pris en compte !), `deny any`, `exit`.

`int g0/0/0`, `ip nat outside`, `exit`, `int g0/0`, `ip nat inside`, `exit`.
`ip nat inside source list 2 interface g0/0/0 overload`, `ip route 0.0.0.0 0.0.0.0 g0/0/0`, `int g0/0`, `no shut`.

## PC23

IP configuration:

- Static et IPv6
- 2001:db8:acad:220::1 /64
- 2001:db8:acad:220::2
- 2001:db8:acad:220::2

## S2-RT-01

en > conf t > host S2-RT-01

`ip domain-name S2.Formation.local`

Si jamais on est sur une vieille version : `int g0/0`, `ipv6 enable`, `exit` et `int g0/0/0`, `ipv6 enable`, `exit`.

`no ip routing`, `ipv6 unicast-routing`, `int g0/0`, `ipv6 add 2001:db8:acad:220::2/64`, `no shut`, `exit`.
`int g0/0/0`, `ipv6 add 2001:db8:acad:200::2/64`, `no shut`, `exit`.
`ipv6 route ::/0 2001:db8:acad:200::254` (la route par défaut, indiquer directement l'adresse IP de l'interface à contacter (préciser l'interface ne marche pas sur la nouvelle version)).

`int g0/0/0`, `ipv6 add FE80::2 link-local` (discussion entre IPv6 et le reste).
`int g0/0`, `ipv6 add FE80::2 link-local`, `exit`

## PC21

IP configuration:

- Static et IPv4
- 10.10.20.1
- 255.255.255.0
- 10.10.20.254
- 10.10.20.254

## S1-CSW-01

`en`, `conf t`, `int g1/0/12`, `switchport trunk encapsulation dot1q`, `switchport mode trunk`, `switchport trunk native vlan 20`, `no shut`.

## S1-CSW-02

`en`, `conf t`, `int g1/0/12`, `switchport trunk encapsulation dot1q`, `switchport mode trunk`, `switchport trunk native vlan 20`, `no shut`.

## S1-SW-03

`en`, `conf t`, `int range f0/22-24`, `switchport mode trunk`, `switchport trunk native vlan 20`, `channel-protocol lacp`, `channel-group 1 mode passive`, `no shut`.

## SLAAC

**A voir dans les slides du cours au slide 129** _mais vu qu'il y a précision de static, normalement on en a pas besoin dans le contexte demandé._

_**Si jamais le prof le demande et qu'on est dans un contexte similaire, bien faire attention si PC23 à l'indication STATIC ou pas.**_
_**Si pas indiqué, alors SLAAC est nécessaire.**_

### S1-RT-01

`en`, `conf t`, `int g0/0`, `no ipv6 nd managed-config-flag`, `no ipv6 nd other-config-flag`, `mac-address 0000.5555.5555`.

### PC23

IP configuration: Auto Config et IPv6.
Le message `Ipv6 Autoconfig request successful`.

## Sécurité (pas oublier de le faire)

Ici on est à environ 90% de configuré sans la sécurité.
La configuration du S1-SW-03 est à copier-coller partout.

### S1-SW-03

`en`, `conf t`, `username admin secret class`, `enable secret cisco`.

Accès SSH pour le VLAN IT :

- `access-list 1 permit 10.10.20.0 0.0.0.255`,
- `crypto key generate rsa`:
  - > 2048.
- `line vty 0 15`:
  - `transport input ssh`,
  - `access-class 1 in`,
    - `login local`.

## ISP (pas toucher !)
