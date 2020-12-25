# odi-12c-password-tools

Oracle Data Integrator 12c Password Tools.

Software uses ODI classes to perform tasks (see `dependencies` section below).

Available commands:
* encode
* decode
* hashWithSalt
* encrypt
* decrypt
* hash
* decryptSalt
* verify

In case of `encode`, `decode`, `hashWithSalt` encryption parameters are not necessary. 

`decode` might come in handy to decode passwords contained in `C:\Users\<ODI_USER>\AppData\Roaming\odi\oracledi\snps_login_work_12.xml`. 

Passwords contained in `SELECT * FROM <ODI_REPO>.SNP_CONNECT` can be decrypted using `decrypt` command.

Passwords contained in `SELECT * FROM <ODI_REPO>.SNP_USER` can be:
* as of Oracle Data Integrator 12.2.1.2.6 decrypted using `decrypt` command
* as of Oracle Data Integrator 12.2.1.4.0 verified using `verify` command (passwords are hashed using `PBKDF2WithHmacSHA256`)

Encryption parameters can be extracted by running following query:
```
select ENC_ALG, ENC_KEY_LEN, ENC_KEY, ENC_IV from <ODI_REPO>.SNP_LOC_REP;
```

Those parameters can be passed to password tool explicitly in command (`-encAlgo`, `-encKeyLen`, `-encKey`, `-encIV`) or in file (`-encProperties`). 

Usage:
```
./odi-12c-password-tools.sh -help
./odi-12c-password-tools.sh -cmd="encode" -password="test"
./odi-12c-password-tools.sh -cmd="decode" -password="dTyaXMpB1FwOG6.iIsGu"
./odi-12c-password-tools.sh -cmd="hashWithSalt" -password="test" -salt="3pCaxQ3Y0OnivH1HgWenP8OjUivgjDOy/dVU02ZIfPU="
./odi-12c-password-tools.sh -cmd="encrypt" -password="test" -encAlgo="AES" -encKeyLen="128" -encKey="aRgHFh8hrXM3GeLvP7Aqxg7,IfZakJO,PAoHn,1PXw" -encIV="zcL89phw9gp5QwoOA7H+6w=="
./odi-12c-password-tools.sh -cmd="decrypt" -password="YZtFu8yth72YMAFmdbOziA==" -encAlgo="AES" -encKeyLen="128" -encKey="aRgHFh8hrXM3GeLvP7Aqxg7,IfZakJO,PAoHn,1PXw" -encIV="zcL89phw9gp5QwoOA7H+6w=="
./odi-12c-password-tools.sh -cmd="hash" -password="test" -encAlgo="AES" -encKeyLen="128" -encKey="aRgHFh8hrXM3GeLvP7Aqxg7,IfZakJO,PAoHn,1PXw" -encIV="zcL89phw9gp5QwoOA7H+6w=="
./odi-12c-password-tools.sh -cmd="decryptSalt" -password="Mwi1ddAuIpfJJZox0yPBdJUujoiaYWFST/cBbU7ZUHzFMvzF/+W9Dtp3KnambDEB:+eqMVFahWLqvi7bLGgjFeIZLybr0RaguBQJj1DsO5Vk=" -encAlgo="AES" -encKeyLen="128" -encKey="aRgHFh8hrXM3GeLvP7Aqxg7,IfZakJO,PAoHn,1PXw" -encIV="zcL89phw9gp5QwoOA7H+6w=="
./odi-12c-password-tools.sh -cmd="verify" -password="test" -hPassword="Mwi1ddAuIpfJJZox0yPBdJUujoiaYWFST/cBbU7ZUHzFMvzF/+W9Dtp3KnambDEB:+eqMVFahWLqvi7bLGgjFeIZLybr0RaguBQJj1DsO5Vk=" -encAlgo="AES" -encKeyLen="128" -encKey="aRgHFh8hrXM3GeLvP7Aqxg7,IfZakJO,PAoHn,1PXw" -encIV="zcL89phw9gp5QwoOA7H+6w=="
./odi-12c-password-tools.sh -cmd="encrypt" -password="test" -encProperties="enc.properties.example"

``` 

Sample encryption properties file:
```
ENC_ALG=AES
ENC_KEY_LEN=128
ENC_KEY=aRgHFh8hrXM3GeLvP7Aqxg7,IfZakJO,PAoHn,1PXw
ENC_IV=zcL89phw9gp5QwoOA7H+6w==
``` 

## dependencies

`odi-core.jar`, `odi-standalone-agent.jar` libraries can be found in your local Oracle Data Integrator 12.2.1.4.0 installation directory (`<ORACLE_HOME>/odi/agent/lib/odi-standalone-agent.jar`, `<ORACLE_HOME>/odi/sdk/lib/odi-core.jar`).

Oracle Data Integrator 12.2.1.4.0 install can be found [here](https://www.oracle.com/middleware/technologies/data-integrator-downloads.html).

In order to compile place `odi-core.jar`, `odi-standalone-agent.jar`  into `lib` folder.
 
In order to use place `odi-core.jar` as well as `args4j-2.33.jar`, `commons-codec-1.11.jar` ([mvn repository](https://mvnrepository.com/)) into `dist/lib` folder.

## note

The software is provided "AS IS", without warranty of any kind. I do not hold responsible for use of this software.