# [](https://github.com/archguard/scanner/compare/v1.6.1...v) (2022-04-18)



## [1.6.1](https://github.com/archguard/scanner/compare/v1.5.0...v1.6.1) (2022-04-18)


### Bug Fixes

* fix newline issue in Windows ([6f4136a](https://github.com/archguard/scanner/commit/6f4136a1ff67afe864359bf4d4f1db7b03de23d8))
* fix origin package error issues ([b06556f](https://github.com/archguard/scanner/commit/b06556f2a8ea1eb380fa07bfa49af8ce375aadc0))
* fix package issues ([b413276](https://github.com/archguard/scanner/commit/b4132762b7ad446fa4fe984cec50525089bc93cc))
* **mybatis:** fix cannot parse local dtd issue ([c902629](https://github.com/archguard/scanner/commit/c902629b9c43b1aa66652457beb9801246b3c0bd))
* **mybatis:** fix dir with xml issues ([9c3224f](https://github.com/archguard/scanner/commit/9c3224f2662be005aa8e271689ba4ba0ba04ca3a))
* **mybatis:** fix empty code issues ([1d52481](https://github.com/archguard/scanner/commit/1d524814c6f9bfc58e6fb5c19dc76de08ea1bedc))
* **sourcecode:** fix field name use keyvalue issue ([f7eedb9](https://github.com/archguard/scanner/commit/f7eedb9b06ed2d874ffba5883bb05c2a87b6f9c1))
* **sourcecode:** fix get a error node name for Kotlin ([5d87b5e](https://github.com/archguard/scanner/commit/5d87b5e2b2748b278390e45cab2b538ce4c9dabd))
* **sourcecode:** fix simple fix for newline issue in sql ([b4a0eef](https://github.com/archguard/scanner/commit/b4a0eefcce0018b093770cc4b5c695b9851b6a1c))
* **sourcecode:** remove api CODE ([88957e8](https://github.com/archguard/scanner/commit/88957e8e28f421f2e3d1a77d4120daf06e6417ed))
* **sourcecode:** remove api only scanner ([24b8feb](https://github.com/archguard/scanner/commit/24b8feb2e437b18844984d026bb02b7546659932))


### Features

* **bytcode:** add test cases for code treee annotation ([f8aca10](https://github.com/archguard/scanner/commit/f8aca10821f71e95110094d2f62eca0b442275df))
* **kotlin:** add visit for rule ([1292ec4](https://github.com/archguard/scanner/commit/1292ec4cd429e2c5bf88629579e394e023a18571))
* **rule:** add basic test smell provider ([ceeff9d](https://github.com/archguard/scanner/commit/ceeff9d85f81252de8594ca42d530d2967990553))
* **rule:** add callback for save results for [#4](https://github.com/archguard/scanner/issues/4) ([86b42c1](https://github.com/archguard/scanner/commit/86b42c1882d82413f90d711c5b8fed649808eed8))
* **rule:** add language for tbsrule [#4](https://github.com/archguard/scanner/issues/4) ([c6eadb2](https://github.com/archguard/scanner/commit/c6eadb234e585996df1b7439b709c446787f94a6))
* **rule:** add sample for test bad smells ([8cf0de3](https://github.com/archguard/scanner/commit/8cf0de326702fd146d3cba7fd3031bcda1483ec3))
* **rule:** add smell position for  [#4](https://github.com/archguard/scanner/issues/4) ([bf21462](https://github.com/archguard/scanner/commit/bf21462bbd63c8345775ded1839e303b48540448))
* **rule:** enable rules for visit ([cb722cd](https://github.com/archguard/scanner/commit/cb722cd89a2c90c07db589a4f845d9f1c2f5d253))
* **rule:** init basic rule cores ([77ce71d](https://github.com/archguard/scanner/commit/77ce71d93e80d59d25c00ec8841c5fc421bbc972))
* **rule:** init context for TbsRuleVisitor ([18072ef](https://github.com/archguard/scanner/commit/18072efeaa6f449645ec5fdcc78f9cbf4e9c1e16))
* **rule:** split impl mods ([9c8abe0](https://github.com/archguard/scanner/commit/9c8abe0ab40658e838c6dc77dde84dff1615ec41))
* **rule:** use visit function for node ([81ef133](https://github.com/archguard/scanner/commit/81ef13373c05130f5729da72a8a6c5e10ad479c6))
* **sourcecode:** add first runner tests ([64bc86e](https://github.com/archguard/scanner/commit/64bc86ee47b7d310b6e5e9c223614a6c627bb8c3))



# [1.5.0](https://github.com/archguard/scanner/compare/v1.4.5...v1.5.0) (2022-04-13)


### Bug Fixes

* **db:** fix insert statement issue ([067bef9](https://github.com/archguard/scanner/commit/067bef969a5e76fbd3f3245ddacf37fbddd77e6e))
* **diff:** fix scanner issues ([f471e20](https://github.com/archguard/scanner/commit/f471e20f2b22f50d9cc35d17ffc33e1ffa19719f))
* **mybatis:** fix test archguard/archguard# ([118eb97](https://github.com/archguard/scanner/commit/118eb973fb721a1726bf51706ecb7fc554d03b88))
* **mybatis:** parse collection from attributes archguard/archguard[#9](https://github.com/archguard/scanner/issues/9) ([181b378](https://github.com/archguard/scanner/commit/181b3782ca98f390c1ae6da4ce1b337d7289e81d))
* remove unused code ([a08cf79](https://github.com/archguard/scanner/commit/a08cf793f5bb43cdab187188d43608750005a436))


### Features

* **api:** add old spring mvc annotation sample ([d519dcd](https://github.com/archguard/scanner/commit/d519dcd9f4e37733929bb3a447dd11e9a2fa357a))
* **database:** add tablename downgrap support ([4d9d897](https://github.com/archguard/scanner/commit/4d9d8971dbef408bb92013af664fa78821b0d905))
* **diff:** add short check for revision id ([d3368f4](https://github.com/archguard/scanner/commit/d3368f4e487592ab914763c33f1ac36d22707eed))
* **git:** enable logs for debug ([0eed4ea](https://github.com/archguard/scanner/commit/0eed4ea3fe58f540cf8b0b025da45574ebc89f45))
* **linter:** init source casing checker ([5f19111](https://github.com/archguard/scanner/commit/5f19111845e34cce15ce41485a9c490f48b66ba3))
* **mybatis:** add baisc node handler [#9](https://github.com/archguard/scanner/issues/9) ([62b597a](https://github.com/archguard/scanner/commit/62b597ad585f3b4d57d72ed165ed5cc833174186))
* **mybatis:** add from path support archguard/archguard# ([214f434](https://github.com/archguard/scanner/commit/214f43499ad883008ce7c1094446bc0aa6d8061e))
* **mybatis:** add mybatis for try generate sql ([f09134a](https://github.com/archguard/scanner/commit/f09134a591aaa3cc559f04bb50f9257453fdea3d))
* **mybatis:** add selectkey support archguard/archguard[#9](https://github.com/archguard/scanner/issues/9) ([d494ae2](https://github.com/archguard/scanner/commit/d494ae2caf5506a652f325966d407d01d2046c76))
* **mybatis:** change output to map archguard/archguard[#9](https://github.com/archguard/scanner/issues/9) ([8e79200](https://github.com/archguard/scanner/commit/8e79200dd7338fa43d6e0846c03e7ea60fcf1d32))
* **mybatis:** clean code archguard/archguard[#9](https://github.com/archguard/scanner/issues/9) ([cf67c98](https://github.com/archguard/scanner/commit/cf67c98fa80c84d373857c069d1f31e2e1dcaa18))
* **mybatis:** enable base parameters for common sql archguard/archguard[#9](https://github.com/archguard/scanner/issues/9) ([5c583a9](https://github.com/archguard/scanner/commit/5c583a979b4934b8cdaf5dd5c67889331d200b31))
* **mybatis:** enable process in db archguard/archguard# ([cb2124f](https://github.com/archguard/scanner/commit/cb2124fe42894818f5bee870bdb73dc8a9d92520))
* **mybatis:** ignore apply for catch issues for archguard[#9](https://github.com/archguard/scanner/issues/9) ([666491c](https://github.com/archguard/scanner/commit/666491ccf69bf834550d25c210bdcd9150e9d448))
* **mybatis:** re add fake parameters archguard/archguard[#9](https://github.com/archguard/scanner/issues/9) ([5203cb2](https://github.com/archguard/scanner/commit/5203cb23f6c3ad15485e0e8daf53eba027e67a97))
* **mybatis:** skip generator for key  archguard/archguard[#9](https://github.com/archguard/scanner/issues/9) ([8fdf711](https://github.com/archguard/scanner/commit/8fdf7112ccdab8f242aad9dcd579246403f75ea1))
* **mybatis:** support for collection archguard/archguard[#9](https://github.com/archguard/scanner/issues/9) ([4cec7dc](https://github.com/archguard/scanner/commit/4cec7dca349dc35513a88369e48ad37da6e33223))
* **mybatis:** support for includes archguard/archguard[#9](https://github.com/archguard/scanner/issues/9) ([160fd19](https://github.com/archguard/scanner/commit/160fd19fc2537bc2fd1dd2917d1668ae3671a75c))
* **mybatis:** test for ognl to parse but not works ([3328b22](https://github.com/archguard/scanner/commit/3328b2239d6a12c0124f8153309bb5f9764b75d0))
* **mybatis:** try parse condition from ognl ([025b6ee](https://github.com/archguard/scanner/commit/025b6ee4d22125481db8b1fe2c9f5d4ca2f1e99b))
* **mybatis:** try parse mybatis code & and crud to key ([c19122c](https://github.com/archguard/scanner/commit/c19122cc47d2537f7e9efb20c06cf87f163a5a90))
* **mybatis:** try parse parmeter for archguard/archguard[#9](https://github.com/archguard/scanner/issues/9) ([540a725](https://github.com/archguard/scanner/commit/540a725f7e307adff693a543f70bd00094f02b64))
* **mybatis:** try to parse by iterations archguard/archguard[#9](https://github.com/archguard/scanner/issues/9) ([e17a09d](https://github.com/archguard/scanner/commit/e17a09d0b7520dd291f8f97f3d2830a349b75989))
* **mybatis:** try write rootNode builder ([969eda7](https://github.com/archguard/scanner/commit/969eda74dda92cb4b937177522086ec19402cd59))
* **mybatis:** use official test caes archguard/archguard[#9](https://github.com/archguard/scanner/issues/9) ([02f8b04](https://github.com/archguard/scanner/commit/02f8b04eeb5997d9a12daf19d96996ff80a67bb4))
* **xml:** init basic design for xml parser ([099552d](https://github.com/archguard/scanner/commit/099552d5d6934bd89889d80b5fe62b7b104f1487))
* **xml:** init parser start element sample for mybatis ([8a281b5](https://github.com/archguard/scanner/commit/8a281b5b3c2f907a6654ae10018f3ffb7b38c60d))



## [1.4.5](https://github.com/archguard/scanner/compare/v1.4.4...v1.4.5) (2022-04-10)


### Bug Fixes

* **change:** fix diff size issues ([74872f9](https://github.com/archguard/scanner/commit/74872f966d34249bdd7709b0ade8e0906a34b7ba))
* fix loc lost in third parth library ([a1f93f5](https://github.com/archguard/scanner/commit/a1f93f591bb73919e7a5075acdb97485c2bc12a1))
* fix refactor issues ([2f1c395](https://github.com/archguard/scanner/commit/2f1c39541224e2752ef590cc782d217f76b380b6))
* fix seriable issue ([a18122a](https://github.com/archguard/scanner/commit/a18122a64c98f03b8209f85fad1386f2731ad596))
* fix typo ([1ed21f4](https://github.com/archguard/scanner/commit/1ed21f4ac35bdeaa17b802b59a8fe1070856479d))
* fix typo ([377b7e7](https://github.com/archguard/scanner/commit/377b7e7fe945184f78f2f953e83fecf832989930))
* fix typos ([3e9b6cd](https://github.com/archguard/scanner/commit/3e9b6cd56acf36f5e20008cdda0d879814857b1b))
* fix windows import issues ([a1cbe35](https://github.com/archguard/scanner/commit/a1cbe35bd78b4e2657ec9df1731084d289fcca9b))
* **mysql:** fix newline issue for windows ([0c9a789](https://github.com/archguard/scanner/commit/0c9a789e0cc7a9102124f2cf3c4187c7211aed9f))
* **sourcecode:** fix class order issues ([9ee6368](https://github.com/archguard/scanner/commit/9ee63680dc730450b309eaf1be2c7727a0b08a89))
* **sourcecode:** fix condition error issues ([4cd8a45](https://github.com/archguard/scanner/commit/4cd8a45aad4bdede25899aa02603ed0360592b96))
* **typescript:** fix fields issues ([443d4fb](https://github.com/archguard/scanner/commit/443d4fb7e2778b48f9f84bc52d72a8764eca1ad5))
* **typescript:** fix tests ([1c60a2d](https://github.com/archguard/scanner/commit/1c60a2d2f96d997cd899346a09f15f687a621605))
* **typescript:** ignore field naming or error nam ([739ee9b](https://github.com/archguard/scanner/commit/739ee9b8ed09766dbb6cb8e04b3dde1c6c484cf8))


### Features

* **diff:** init repository ([18922d6](https://github.com/archguard/scanner/commit/18922d6497081a4a9ee698a7b74345f42aa9e6ac))
* **diff:** init repository save ([6decf6d](https://github.com/archguard/scanner/commit/6decf6d7863299692c3e47cca410c49c1105b3da))
* **diff:** make first diff changes ([fcd00db](https://github.com/archguard/scanner/commit/fcd00dbd9df71923a9a2dd909dc230b344eea9f2))
* **sourcecode:** change thirdpart access to public ([d172bf8](https://github.com/archguard/scanner/commit/d172bf8544245b2fc1733b92b7e132dcf18295da))
* **typescript:** add exports to name support ([6e7e6d2](https://github.com/archguard/scanner/commit/6e7e6d2ca1299524643d0f54daf07a71da9e07d8))
* **typescript:** add normal component support ([99678d8](https://github.com/archguard/scanner/commit/99678d8299546df1777d09ae84e43649202b4f1a))
* **typescript:** add usage name support ([31d7ac3](https://github.com/archguard/scanner/commit/31d7ac36251083f63ffb218e854846f2282ec76b))



## [1.4.3](https://github.com/archguard/scanner/compare/v1.4.2...v1.4.3) (2022-04-06)


### Bug Fixes

* **api:** fix empty requestMapping ([69975fb](https://github.com/archguard/scanner/commit/69975fbaf365ba68ad89d7fea2451a6ddcdf8c72))
* fix typos ([54021f6](https://github.com/archguard/scanner/commit/54021f62dc5e3e6ff8a5c1326beb51586922b041))


### Features

* add basic change compare & wait for chapi 1.5.33 :sleepy::sleepy::sleepy::sleepy: ([132ba56](https://github.com/archguard/scanner/commit/132ba56855d9d495fbe14bdcb82c30613deebe42))
* add differ for changed files ([c8f24f7](https://github.com/archguard/scanner/commit/c8f24f7e97353df24fead5ceb76a2a6b50e72393))
* add differ for list names ([0f39383](https://github.com/archguard/scanner/commit/0f39383e59eea18232cf46b4a8d21a2d6783393b))
* **differ:** add simple converter for items ([07d78a2](https://github.com/archguard/scanner/commit/07d78a2dbb8d00fbc81ddb1e3875674e985f373e))
* **differ:** init basic logic, but need to update compare logic in chapi ([2c826dd](https://github.com/archguard/scanner/commit/2c826dd3f7d14861f777706c3567b431aa341631))
* **differ:** init first commits for test in GitHub actions ([21ee575](https://github.com/archguard/scanner/commit/21ee575a24df4f834724330f7ce6e14a471af57b))
* **differ:** init parameters ([d887eb7](https://github.com/archguard/scanner/commit/d887eb7835f12301713b93c88ed7efa52ddf39cc))
* make differ return changed functions ([c4f0a1b](https://github.com/archguard/scanner/commit/c4f0a1ba6e5203ea696d1666908af3c558d1360f))
* make filter changed ds works ([3acf9b3](https://github.com/archguard/scanner/commit/3acf9b33f29497d3d7fb0f1705ceb41ded200c17))
* make first version rcalls ([8c83dfd](https://github.com/archguard/scanner/commit/8c83dfdd709549e410ce9c01405e07490e20ffdc))
* make generte changes work snapshot versions ([bbf1627](https://github.com/archguard/scanner/commit/bbf162795502a2896c60ce6ce4552a308107b6ab))
* remove compare for function sized ([68d0354](https://github.com/archguard/scanner/commit/68d035479d98cd1b8b849a74bb3981988a12deba))
* use chapi to parse data structs ([3002663](https://github.com/archguard/scanner/commit/3002663fc934a12b7f6491e801701abf8145af77))



## [1.4.2](https://github.com/archguard/scanner/compare/v1.4.1...v1.4.2) (2022-04-02)


### Features

* **datamap:** add jpa @Query ([ddc569c](https://github.com/archguard/scanner/commit/ddc569c8f1c70276351b7ab43d173fdf2de74e34))
* **datamap:** fix backtick in table name ([fc354b7](https://github.com/archguard/scanner/commit/fc354b7ebebe1b296443a37b30b3c3bb85a68715))
* **datamap:** fix close ([b51b266](https://github.com/archguard/scanner/commit/b51b26603a832b1dde1c31da9e9591b5c36843d2))
* **datamap:** fix field typo ([82f87de](https://github.com/archguard/scanner/commit/82f87dee38138fcee01c2c2b839ba6cbf851ba69))
* **datamap:** init code db relation repository ([a690b9f](https://github.com/archguard/scanner/commit/a690b9f26e58bbe2f290180986fe7ed5c54c2a07))
* **differ:** init module ([b1eff2b](https://github.com/archguard/scanner/commit/b1eff2b0374941be617adf525266148d7a0043ec))
* make save to projects ([97d1ff5](https://github.com/archguard/scanner/commit/97d1ff567206f63fdd05dd089fba85bc4c5b2808))



## [1.4.1](https://github.com/archguard/scanner/compare/v1.4.0...v1.4.1) (2022-03-31)


### Bug Fixes

* fix build ([4dc6bf5](https://github.com/archguard/scanner/commit/4dc6bf53367c9931f3e1b2268733eda935e28dad))
* fix query issue for  without quote ([fb6bbfe](https://github.com/archguard/scanner/commit/fb6bbfe6070f94ab8cb148fc5dc48a32cc0e385f))
* fix tests ([e67a8d3](https://github.com/archguard/scanner/commit/e67a8d3c420a231fecb029ecb804cebf9319d1d8))
* remove unused codes ([d17a0f4](https://github.com/archguard/scanner/commit/d17a0f472ea2ede8f1a6d8d0b0c9a1afbb17dc0c))


### Features

* **database:** add first db analyser simple ([ce0b754](https://github.com/archguard/scanner/commit/ce0b7542823e00f7c478eaba18d499d233b81869))
* **database:** add first query tests ([3b66265](https://github.com/archguard/scanner/commit/3b662651c933d0624559b26d325a47e92dec41a1))
* **database:** add first query testsgst ([ba4917b](https://github.com/archguard/scanner/commit/ba4917bcf44007033da5ec0753689ac59b2cc36b))
* **database:** add handle for exception ([c5e34ba](https://github.com/archguard/scanner/commit/c5e34ba6a1aba9b19f5e96bd326c932b516e4084))
* **database:** add handle for raw string ([1c77596](https://github.com/archguard/scanner/commit/1c77596193199583514bcc30e7fa97b6c44dbd43))
* **database:** add handle for raw string in sql ([dbbb57e](https://github.com/archguard/scanner/commit/dbbb57ec966f6b6ffb9a49dda62f3643d4a4ca7d))
* **database:** add log to dbs ([bc21df2](https://github.com/archguard/scanner/commit/bc21df281b0d7dbf09208e5ba2b5283ce9a82239))
* **database:** add logs for dbs ([4f66b14](https://github.com/archguard/scanner/commit/4f66b148cf2f39ad19920da3fe036941f464058f))
* **database:** add parse for sql ([1fcb58d](https://github.com/archguard/scanner/commit/1fcb58dfdcebfc0372873b063d962a006ca3fce7))
* **database:** add query for createQuery ([49446b7](https://github.com/archguard/scanner/commit/49446b7136bedc11ddb18381c09313342fc82a02))
* **database:** add support for variable inline ([bfe4922](https://github.com/archguard/scanner/commit/bfe4922470a5bba0d7b3495d4eefc53b001309e4))
* **database:** add to runnder ([1e6106c](https://github.com/archguard/scanner/commit/1e6106c7507ea969d5eaeffdc1f65b939d644695))
* **database:** fix item empty ([3ea6161](https://github.com/archguard/scanner/commit/3ea61615bdfb888ff57c38881ee625440390846c))
* **database:** fix kotlin in variable ([0d5e137](https://github.com/archguard/scanner/commit/0d5e13774479ff13ed680aa9476ef00c603a7261))
* **database:** replace items in default ([2bf1d68](https://github.com/archguard/scanner/commit/2bf1d689964ebdd453bd1f0f75304e3db1ae7853))
* **database:** support for jdbi binding ([4549046](https://github.com/archguard/scanner/commit/4549046f39767f125d390363c306b96fba2da1eb))
* **kotlin:** fix end with +" issue ([52b8e0e](https://github.com/archguard/scanner/commit/52b8e0e8c1db0cc90a0f9c73bedf66b6faa5b8f0))
* **kotlin:** fix offest & limit issues ([86d2b5f](https://github.com/archguard/scanner/commit/86d2b5f1cd2301ff0ed376aeb7387d8443905632))



# [1.4.0](https://github.com/archguard/scanner/compare/v1.3.2...v1.4.0) (2022-03-30)


### Bug Fixes

* add lost assert for source caller ([2317f21](https://github.com/archguard/scanner/commit/2317f21fbf44f7624fed92f8b361da8a9486dae2))
* add lost source caller ([cd40461](https://github.com/archguard/scanner/commit/cd40461e0d45e1a69b763b56380f29e8c44721c9))
* **api:** fix <init> function issue ([fc5ea89](https://github.com/archguard/scanner/commit/fc5ea892b77790bf9f8ee1274524646ac0cef6b2))
* **bytecode:** exclude init method to call, but add to import ([8b0bbb7](https://github.com/archguard/scanner/commit/8b0bbb720cbbc50bc3ec667e147054a1a7d2fb26))
* **bytecode:** fix api empty issues ([b6e70de](https://github.com/archguard/scanner/commit/b6e70de5969042809a9bc88f0d076c84dd545b87))
* **bytecode:** fix for interfaces empty issue ([23d6882](https://github.com/archguard/scanner/commit/23d6882b056db84efc70de6a1310429509da7d72))
* **bytecode:** fix import issues ([3dae498](https://github.com/archguard/scanner/commit/3dae498dff0aada456a0bba17e6ebbf0272fe5fa))
* **bytecode:** fix typos ([e3e296c](https://github.com/archguard/scanner/commit/e3e296c0e5ed7e76f811e89e2189245055c95ba4))
* **bytecode:** fix typos ([b4d7a81](https://github.com/archguard/scanner/commit/b4d7a81e0e32c8807ebe4fbefd9d23d57ce6ae2d))
* **bytecode:** remove default types from imports ([f1b8541](https://github.com/archguard/scanner/commit/f1b854127af131f10531bed626428715acae0dab))
* **bytecode:** remove unused $ ([6d12335](https://github.com/archguard/scanner/commit/6d12335f243d9f794e35b81a6a2791732de6948b))
* fix kotlin.Metadata annotation issues ([ceb792f](https://github.com/archguard/scanner/commit/ceb792f75ebde59cd96f0a0a350d68d1d5c0ee44))
* fix lints ([497291d](https://github.com/archguard/scanner/commit/497291d4d7b023721e987f2871e5b0b6ad864e9e))
* fix lowercase issue for function name ([d31422b](https://github.com/archguard/scanner/commit/d31422bdb35ce4248a33b968f0ecfa9691c5160e))
* fix typos ([a46658e](https://github.com/archguard/scanner/commit/a46658e501d38e7821b20f2aa9bc70a377b7c771))


### Features

* add basic call support for java ([d8c9295](https://github.com/archguard/scanner/commit/d8c92957428bf90976c5dbaa51b10866f93d2dd6))
* add code sample for data ([1f67d84](https://github.com/archguard/scanner/commit/1f67d8492b810804d4841e27606fe1eaa29b4127))
* add line position for method node ([e66a8f6](https://github.com/archguard/scanner/commit/e66a8f6e190567a4ad738e1c8b2836a927cd1343))
* add test functions for java ([fecf421](https://github.com/archguard/scanner/commit/fecf421353e85de9ce560f73fc76517966d370bc))
* **bytecode:** add annotation support ([11e0f50](https://github.com/archguard/scanner/commit/11e0f50ba6915df0180ac4c11967bd723fabcaa6))
* **bytecode:** add class modifier parser ([26ae47c](https://github.com/archguard/scanner/commit/26ae47cefe572b27a6c5f82add5dabd916ecfb73))
* **bytecode:** add constuctor check ([af8f13a](https://github.com/archguard/scanner/commit/af8f13a0be1bae023d0304889b2ef5610dcd7272))
* **bytecode:** add field support ([bdd75e4](https://github.com/archguard/scanner/commit/bdd75e48c69852b28108bafea52b3fd89c260935))
* **bytecode:** add for rest template ([2bf2c04](https://github.com/archguard/scanner/commit/2bf2c0419cecffef71923675963fd77a281d00e5))
* **bytecode:** add get classes support ([82fb58a](https://github.com/archguard/scanner/commit/82fb58ac748d8df9edacc5f351f3480057802738))
* **bytecode:** add method modifier support ([0d36706](https://github.com/archguard/scanner/commit/0d3670638831edf5d043b679738ae203165ea1aa))
* **bytecode:** add modifier support ([7bd03bc](https://github.com/archguard/scanner/commit/7bd03bc46d66fe199b0dbd76f09eee72821a3c01))
* **bytecode:** add parameters type support ([c55b649](https://github.com/archguard/scanner/commit/c55b6490a82a2e3105c170835bffd452aa4559ca))
* **bytecode:** add return type support ([270970a](https://github.com/archguard/scanner/commit/270970a7a2b693bd4d4dfddb6ad912dc82aec999))
* **bytecode:** add simple constant pool ([eecafb3](https://github.com/archguard/scanner/commit/eecafb35c55594658b13305835d241c1a884fd43))
* **bytecode:** add simple constant to TypeValue ([19e13e4](https://github.com/archguard/scanner/commit/19e13e422f2b8b528f501577ab66bb533b628b6b))
* **bytecode:** add test for module util ([6dbf54f](https://github.com/archguard/scanner/commit/6dbf54f4b410f1ad337c1c72d1c4b49d62cc24ec))
* **bytecode:** add url in array for annotaion support ([0e9bcc5](https://github.com/archguard/scanner/commit/0e9bcc515d814e691ba9103c4392c632230ae673))
* **bytecode:** change annotation to end with import collector ([3a86b78](https://github.com/archguard/scanner/commit/3a86b7819e9729bc7d4668bb7f6f573a7526b7bb))
* **bytecode:** feat class package identify support ([69e2a8f](https://github.com/archguard/scanner/commit/69e2a8f935c22a935b955f552f5e63622e69746b))
* **bytecode:** feat split class name and package name support ([8f7014b](https://github.com/archguard/scanner/commit/8f7014bd651aa67446caf6d652c77dce3d4a9cbb))
* **bytecode:** ignore init methods for java & kotlin ([22cae37](https://github.com/archguard/scanner/commit/22cae37522bd6e4b6c70a35351036e5c6df134e6))
* **bytecode:** import runner ([f2f985b](https://github.com/archguard/scanner/commit/f2f985bb03b57d3cb560bc527fb806b337b1769e))
* **bytecode:** init for scala samples ([87d55a5](https://github.com/archguard/scanner/commit/87d55a51e9212b5f1718eaef6483f81d3a0b5eea))
* **bytecode:** init import calls ([79720f2](https://github.com/archguard/scanner/commit/79720f2aa4e6bee6187cebe9a668bb3dfc01fee4))
* **bytecode:** make first sample for workdirs ([0076205](https://github.com/archguard/scanner/commit/0076205ba1a36167f1dd89104e1790de733a66a9))
* **bytecode:** reinit module parser ([0e5a244](https://github.com/archguard/scanner/commit/0e5a244c8656b85d16c8dce0fe66fe14963fbce8))
* **bytecode:** split imports ([53676c9](https://github.com/archguard/scanner/commit/53676c9ee14dcfec10d414eb383dee583be0f7fe))
* **bytecode:** try to add nested name ([0ec64aa](https://github.com/archguard/scanner/commit/0ec64aa746072801ba0ed495fabcd9ff54345114))
* init first bytecode samples ([9082f2d](https://github.com/archguard/scanner/commit/9082f2d1c826802d6db44260ccb0c65dbd961ae9))



## [1.3.2](https://github.com/archguard/scanner/compare/v1.3.1...v1.3.2) (2022-03-25)


### Bug Fixes

* empty http method for match more than one ([53045de](https://github.com/archguard/scanner/commit/53045de57b058009d162f9c0f200e67dfa64107d))
* fix empty http method issue ([deb1f6c](https://github.com/archguard/scanner/commit/deb1f6cc431820c9d2d103879e35460b5ee2ecc2))
* update tbs chapi versions ([ed5f8f2](https://github.com/archguard/scanner/commit/ed5f8f229d60c90675a39718d11523a6ffe05737))



## [1.3.1](https://github.com/archguard/scanner/compare/v1.2.4...v1.3.1) (2022-03-25)


### Bug Fixes

* empty default language to skip complex ([e3b84f8](https://github.com/archguard/scanner/commit/e3b84f81f47b3ba582c189230b21f4577ce22502))
* fix // issues in api ([274271f](https://github.com/archguard/scanner/commit/274271f1b6bc02e1812973904cf041d13beadfcb))
* fix api define issues ([be834fa](https://github.com/archguard/scanner/commit/be834fa87839b1046524ff410f41f0c795726a06))
* fix build name issues ([c0023c4](https://github.com/archguard/scanner/commit/c0023c472d23ffdf334b47107ce0791931962f9c))
* fix ci ([76b5bc9](https://github.com/archguard/scanner/commit/76b5bc93d28fc0aca339db870e7b0c0395f30d62))
* fix claname isuses ([c4155b4](https://github.com/archguard/scanner/commit/c4155b4497377b3426d5da90cbb887a5582c77b2))
* fix ext to languages not a list issues ([81bb0c2](https://github.com/archguard/scanner/commit/81bb0c25fa6fcc06433e68dbe7065802ca539462))
* fix json file with keywords issues ([e198995](https://github.com/archguard/scanner/commit/e198995c003cc0e5745bb37fc6d069ef19727fba))
* fix lost repo for resoruce issue ([40e88b8](https://github.com/archguard/scanner/commit/40e88b8ce867bc1178221285def1b4252fdfa09b))
* fix open image files issues ([fb31740](https://github.com/archguard/scanner/commit/fb31740062566d0e9510fa5dda1c796b074660fa))
* fix route issues ([521ef20](https://github.com/archguard/scanner/commit/521ef20ce28dbd1453912206632fa244bd468dd3))
* fix some controller not annotation issue ([548f155](https://github.com/archguard/scanner/commit/548f155324d621037e0b49a5eac82ac4a570e318))
* fix typos ([3a29f5f](https://github.com/archguard/scanner/commit/3a29f5f63aa1c6d31edfbefd0e20e2560b4a109d))
* remove unused with api flags ([0acd089](https://github.com/archguard/scanner/commit/0acd0894c8b84530ff5a3df7028c64658a3229e8))


### Features

* add basic second ext for try ([54de4fc](https://github.com/archguard/scanner/commit/54de4fc5e4515dca9b9267e2cffb5c1b5dd87aa2))
* add check for .dot file ([75d3a80](https://github.com/archguard/scanner/commit/75d3a80985612d5b3f978a9225b0ebb8c13c3484))
* add java api scan support ([482ba8e](https://github.com/archguard/scanner/commit/482ba8e1a80dbe736105b4465f0564829ac0ec20))
* add line added and line deleted support ([75c9696](https://github.com/archguard/scanner/commit/75c96963fad04045ef99c615deea8e66adaa3314))
* add pure filename support ([75d9f7f](https://github.com/archguard/scanner/commit/75d9f7f06e779b3682e34ca337442d814d8a6846))
* add test for license file ([d443ec3](https://github.com/archguard/scanner/commit/d443ec311257ea519c748adffb573dde8b73a6b9))
* **c#:** add loc for class support ([2cab6ea](https://github.com/archguard/scanner/commit/2cab6ea215d565e16ee99101faac00636ae4d89b))
* **collector:** init ci ([0882295](https://github.com/archguard/scanner/commit/08822951924fb12cfc6bae4d3988730b284298e6))
* init c# api parser ([a45736c](https://github.com/archguard/scanner/commit/a45736c1c704f2f88fcaed9ca436e7b085e92c3b))
* **java:** add sub url issue ([8ba6f23](https://github.com/archguard/scanner/commit/8ba6f23b11f1ae8ebbdd8efc7dce2132bc4b6d22))
* make classComplexity only for java langauge ([cca5307](https://github.com/archguard/scanner/commit/cca5307c05a228ce783861424d1aad631427c68c))
* **sourcecode:** add api only flags ([ef72630](https://github.com/archguard/scanner/commit/ef72630487d8143a15cc087ef3d27c2e58e81e03))
* **sourcecode:** add for gbk count lines support ([42005b8](https://github.com/archguard/scanner/commit/42005b8d7a20de8df9ff5fa9db30eb78bd5b0b77))



## [1.2.4](https://github.com/archguard/scanner/compare/v1.2.3...v1.2.4) (2022-03-23)


### Bug Fixes

* fix jgit crash issues ([3ceb62f](https://github.com/archguard/scanner/commit/3ceb62fc67ec64c67b3b3fb86dbaa6527a62d350))
* fix tests ([cba9f90](https://github.com/archguard/scanner/commit/cba9f902a3c9968ce4b9dfc641692e3ba420601d))


### Features

* add append to files for content ([3def653](https://github.com/archguard/scanner/commit/3def653a251e92bbb3db5516c0d6a6c171f1b45c))
* add languages resoruces ([4606c64](https://github.com/archguard/scanner/commit/4606c647b2ea56862d28e793d4c1d40b094e8e81))
* add line count to file git changes ([de053c4](https://github.com/archguard/scanner/commit/de053c4cdcdbfa5849f6e66740875cbc20869513))
* add lost test languages ([435e95c](https://github.com/archguard/scanner/commit/435e95c535756e303b508b977956e20112560042))
* add recoginize language support ([013468a](https://github.com/archguard/scanner/commit/013468a1f85f20d0fcdab50874e05a1d7b3363d9))
* add simple line counts ([0f98cb8](https://github.com/archguard/scanner/commit/0f98cb8f06c1512097fc637af977110c50cd502d))
* try to fix file type issues ([96f6648](https://github.com/archguard/scanner/commit/96f6648adf919bf97287305768e80923241a8099))



## [1.2.3](https://github.com/archguard/scanner/compare/v1.2.2...v1.2.3) (2022-03-22)


### Bug Fixes

* add lost for save class anotations ([e5a2c22](https://github.com/archguard/scanner/commit/e5a2c22df460e9ff7fdd9f780a18e98dcd378bd2))
* align path convert ([bbfe6f3](https://github.com/archguard/scanner/commit/bbfe6f383da44258a335269cef38d9661db2c991))
* fix arguments types issues ([821609c](https://github.com/archguard/scanner/commit/821609cb4fc6da5763684ff918dd8c5e70c0b234))
* fix container api error issues ([914f870](https://github.com/archguard/scanner/commit/914f870bda6603053db28ee725aec37b5a04a609))
* fix some fields lost issues ([97aadf7](https://github.com/archguard/scanner/commit/97aadf78ddb27f1e9736084cb499306f5c5c11d1))
* fix table not convert issues ([e177adc](https://github.com/archguard/scanner/commit/e177adc81ffc4c0c32f48cb0db1736ec02dd7278))
* fix tests ([99abb0b](https://github.com/archguard/scanner/commit/99abb0bd0ccae43397e768f8f5b944bf13321945))
* fix typescript import issue ([c4574dc](https://github.com/archguard/scanner/commit/c4574dc9d98a1285165b48e0f7710cfb35ee2911))
* fix typos ([0157b14](https://github.com/archguard/scanner/commit/0157b143bd5fd8a761325f42901d6d32fd7c0ebd))
* remove typos for baseUrl ([7b133d5](https://github.com/archguard/scanner/commit/7b133d5b192f03d36c0cae4425abc5f551b71bb3))
* **ts:** set default export to file name ([1b6dc91](https://github.com/archguard/scanner/commit/1b6dc9199bc4b047dfb1b0a1c40a5a888e28acdd))


### Features

* add basic url for frontent api desing ([8cb1254](https://github.com/archguard/scanner/commit/8cb1254e67316a6430b6b396d41f9ca8afc9b98f))
* add convert for import ([c584b98](https://github.com/archguard/scanner/commit/c584b988013cffea1dcfe8e66355d62afeee596e))
* add handle for class deps rehandle ([f8d7f4b](https://github.com/archguard/scanner/commit/f8d7f4ba2f8bbeb7d95a28aab81b2a7454b837d6))
* add intergration for frontent api analyser ([8301f81](https://github.com/archguard/scanner/commit/8301f811f79af620e4c6c7ef9ad65339d3b7a16a))
* add lost system id to system ([d105d9b](https://github.com/archguard/scanner/commit/d105d9bdce8fe30a41579836a7cead2fab6b8ccc))
* add simple fix for system ([13b8ed0](https://github.com/archguard/scanner/commit/13b8ed091288f636872927e73a4ae081838ff4d6))
* fix source method & package issue ([8914b76](https://github.com/archguard/scanner/commit/8914b76c6cb39a35b5507731a4e7ddb97010bf9d))
* import api scanner for frontend ([97c4f77](https://github.com/archguard/scanner/commit/97c4f77c1e496607a15a0f62319f32fde2d80c7e))
* save class in components ([ba1aa00](https://github.com/archguard/scanner/commit/ba1aa00c8b89ec712c2cd49f3c88c95caca79afa))
* **sourcecode:** add javascript import support ([fc44a3e](https://github.com/archguard/scanner/commit/fc44a3ea809952f21db6a2e092a5037669842a06))
* **ts:** enable class name and method name empty ([cf45599](https://github.com/archguard/scanner/commit/cf45599715eca2f4f100ffffe2c5f76e3a10b795))



## [1.2.2](https://github.com/archguard/scanner/compare/v1.2.1...v1.2.2) (2022-03-20)


### Bug Fixes

* add check for same class already in system ([8fd5759](https://github.com/archguard/scanner/commit/8fd5759d0c3f8ce4fe522bddad5e0799e469883c))
* fix db sql config issue ([d8de8a1](https://github.com/archguard/scanner/commit/d8de8a1d503a2ecafaf48323c63be71df9dab35d))
* fix some typos ([3629e5c](https://github.com/archguard/scanner/commit/3629e5c8423235b9ee6cd9660fe04fc82c0f8153))
* fix typo ([5f6f908](https://github.com/archguard/scanner/commit/5f6f908fa28a9d006110297fff9931d2949eaf96))


### Features

* add catch for duplicated key issue ([f343f7c](https://github.com/archguard/scanner/commit/f343f7c6d3734a680db16380312489c743498348))



## [1.2.1](https://github.com/archguard/scanner/compare/v1.2.0...v1.2.1) (2022-03-20)



# [1.2.0](https://github.com/archguard/scanner/compare/v1.1.10...v1.2.0) (2022-03-20)


### Features

* **container:** init api analyser flag ([19caf3b](https://github.com/archguard/scanner/commit/19caf3bdc24c5e519e373eb8c226b3db66f579ea))



## [1.1.10](https://github.com/archguard/scanner/compare/1.1.10...v1.1.10) (2022-03-19)



## [1.1.10](https://github.com/archguard/scanner/compare/v1.1.9...1.1.10) (2022-03-19)


### Bug Fixes

* fix empty issue ([812fdf6](https://github.com/archguard/scanner/commit/812fdf611f81b836dae2963b7510407824efafc8))
* fix loc position issues ([7a74f8d](https://github.com/archguard/scanner/commit/7a74f8d7448f81d5d5218cbfc90ccea66af69206))


### Features

* init basic method callees ([c789603](https://github.com/archguard/scanner/commit/c789603e3a72a1851fc54aa2ca1e71338f5e769e))
* **sourcecode:** add soruce target for package ([8a9a544](https://github.com/archguard/scanner/commit/8a9a544d9b99d84223ec122184a6b383b4d9172c))



## [1.1.9](https://github.com/archguard/scanner/compare/v1.1.8...v1.1.9) (2022-03-18)


### Bug Fixes

* fix file not generate issue ([69500c3](https://github.com/archguard/scanner/commit/69500c343ffbca4e4841fb9cfbb0f5582bdeba55))
* fix value contains \n issue ([1643b78](https://github.com/archguard/scanner/commit/1643b7814ccfd16ad5ada814410fcd01049d1218))
* **jacoco:** fix default path issue ([78af229](https://github.com/archguard/scanner/commit/78af2291a5792e098cc3d88be8d11b46f1b1fee0))



## [1.1.8](https://github.com/archguard/scanner/compare/v1.1.6...v1.1.8) (2022-03-18)


### Bug Fixes

* add lost kotlin ([7f9724a](https://github.com/archguard/scanner/commit/7f9724a26472a66d9ed7f82e256261ce670b573d))
* fix class name & module name issue ([4c51d09](https://github.com/archguard/scanner/commit/4c51d095b32d095a4fe6016cb530ffdad9c1be34))
* fix typo ([404ccda](https://github.com/archguard/scanner/commit/404ccda542c001cf1cd68648972b89dbc81fe129))
* set default module to root for test ([8368c8f](https://github.com/archguard/scanner/commit/8368c8f16b4c1cc75dc2c86e91ea6ca9ed7ea817))
* try to fix versions issues ([cc291a1](https://github.com/archguard/scanner/commit/cc291a19a9af448a48dc874cdb0878095eb62a84))


### Features

* add clz dep ([8455024](https://github.com/archguard/scanner/commit/8455024279b73147d036c2943bc7fa02e1551be6))
* enable source code to data ([1f05712](https://github.com/archguard/scanner/commit/1f0571226e32444eff7104e2454625441b50a277))
* enable TypeScript parse ([154c5d9](https://github.com/archguard/scanner/commit/154c5d99e53737a8e64d4b2e5ff7c280b44735ea))
* **source:** add methodfield support ([62d3f0a](https://github.com/archguard/scanner/commit/62d3f0ad368a7cd36433568339c6197b461d3278))



## [1.1.6](https://github.com/archguard/scanner/compare/v1.1.5...v1.1.6) (2022-03-17)


### Features

* **typescript:** add first version for demand ([b235863](https://github.com/archguard/scanner/commit/b235863edb43783eec774fc5208f148576ee0d9d))



## [1.1.5](https://github.com/archguard/scanner/compare/v1.1.4...v1.1.5) (2022-03-17)


### Bug Fixes

* fix kotlin size count issues ([1f54e5f](https://github.com/archguard/scanner/commit/1f54e5f607e434f87ff702be7b02ed2afaa22454))



## [1.1.4](https://github.com/archguard/scanner/compare/v1.1.3...v1.1.4) (2022-03-16)


### Bug Fixes

* fix typos for runner name ([4fae2fd](https://github.com/archguard/scanner/commit/4fae2fd1bc32be0763de31ebe7c4f11be643f1d8))


### Features

* add test bad smells ([5c3cffb](https://github.com/archguard/scanner/commit/5c3cffbb3dee5f1669ba055c94cd4a0c02750f5f))



## [1.1.3](https://github.com/archguard/scanner/compare/v0.1.2...v1.1.3) (2022-03-16)



## [0.1.2](https://github.com/archguard/scanner/compare/v0.1.1...v0.1.2) (2022-03-16)


### Bug Fixes

* fix deps issues - update chapi to 0.2.0 ([f8d0d39](https://github.com/archguard/scanner/commit/f8d0d39984446b9894e0930b90099c6207141e4d))
* fix typos ([5a61a8e](https://github.com/archguard/scanner/commit/5a61a8e1e6d9fbf1894bf686ef11838aa1c81e1b))


### Features

* add basic mysql parser for database relation ship ([0c283dd](https://github.com/archguard/scanner/commit/0c283dd55b13717e83158cefbe9a51f44c6cbdac))
* add lost system id ([be170bc](https://github.com/archguard/scanner/commit/be170bcf096f3b1666029f9b76f05ec97711e5aa))
* enable to class lists ([572a0d7](https://github.com/archguard/scanner/commit/572a0d7dabf42803ecb1d191f351d8da3400d0c0))
* export first sql files ([ce93dbd](https://github.com/archguard/scanner/commit/ce93dbd3fe9fc6a768c11748a2e61586a805d880))
* **httpapi:** init basic typescript part ([e15c237](https://github.com/archguard/scanner/commit/e15c23772625dec61a8da1df2fc650a875c84060))
* import chapi for parse source ([11f94ca](https://github.com/archguard/scanner/commit/11f94cab93654014b250f3278c8d66e667fbfe96))
* init scan javasource ([f5aab20](https://github.com/archguard/scanner/commit/f5aab20b1ef9e01daa354d92af45f3363f3386ce))
* **javasoucre:** add field support ([498677d](https://github.com/archguard/scanner/commit/498677d69c08a561a8c6726779207fcf467cec01))
* **javasoucre:** add save annotation ([f2edbfc](https://github.com/archguard/scanner/commit/f2edbfc6db550818cbae5ee1e1d3a85e88fb5bd5))
* **javasoucre:** add save method ([c62d2ce](https://github.com/archguard/scanner/commit/c62d2ce8891dc4246d01d2ef271889b1eac734dc))
* use maven center chapi ([0005620](https://github.com/archguard/scanner/commit/000562031208673665c2d07fbe6dd46c1c604cfb))



## [0.1.1](https://github.com/archguard/scanner/compare/v0.1.0...v0.1.1) (2022-02-19)


### Bug Fixes

* fix kotlin clikt no run issue ([bd1b32d](https://github.com/archguard/scanner/commit/bd1b32da27b66de7820e8ed99d21acf67d9e7a6a))
* fix scan jacoco clikt issue ([1f5fd05](https://github.com/archguard/scanner/commit/1f5fd057550c81f537fa073d4973679c14be0cd5))


### Features

* add shadowjar for fatjar ([10d6c42](https://github.com/archguard/scanner/commit/10d6c4269c5312f50ae481f71e97ffb899b5d075))



# [0.1.0](https://github.com/archguard/scanner/compare/a7fdc4ce90dc0d97455fa18a4eb8ae65772f2ab5...v0.1.0) (2022-02-18)


### Bug Fixes

* fix version issue for compatibility ([1e95a9b](https://github.com/archguard/scanner/commit/1e95a9b6b45d9c1778de68e81a93b91a9566a89f))


### Features

* add jacoco as moudle ([600465d](https://github.com/archguard/scanner/commit/600465d148872e9fd8fdbadbff00bc23c4b9d6ac))
* add mem initial to 2g ([1e78746](https://github.com/archguard/scanner/commit/1e787463b2117e2d0b981bb267f9d9f25dffaa53))
* add package ([bd997d9](https://github.com/archguard/scanner/commit/bd997d94b2040c2b62d48eee56f4185ea12cbfed))
* add package ([bb29db8](https://github.com/archguard/scanner/commit/bb29db8047f83b162c09ea7443feefb66f2e8823))
* correct compile issue ([a7fdc4c](https://github.com/archguard/scanner/commit/a7fdc4ce90dc0d97455fa18a4eb8ae65772f2ab5))
* correct main class ([42e0307](https://github.com/archguard/scanner/commit/42e03075db203bdf9eb3a3ca8fed3419ba6b2ab6))
* correct test ([497204a](https://github.com/archguard/scanner/commit/497204a9050ee5cb5cb1d187b57fd50e1bc64471))
* jgit version update ([bfc43a5](https://github.com/archguard/scanner/commit/bfc43a5cad65a8e8d3ad13b437ad6410e704551b))
* remove tbs ([8804e50](https://github.com/archguard/scanner/commit/8804e50556199fd4cb4c82018a7ef5d5a94a8854))
* remove unused dependency ([56a6e4b](https://github.com/archguard/scanner/commit/56a6e4b4ce4c6eaaaf17f48ccb1a3efdbab99af1))
* renmae the output file to jacoco.sql ([8ba1c03](https://github.com/archguard/scanner/commit/8ba1c03d74a6893b534714db7a90643ea53ca4dd))
* try to fix heap issus ([fec7e4a](https://github.com/archguard/scanner/commit/fec7e4a79764654421ecf1ec741e9411414280c3))


### Reverts

* Revert "feat: correct JGitTest" ([a9879d9](https://github.com/archguard/scanner/commit/a9879d9af03072e4e39e55c99b470176c3c6310a))



