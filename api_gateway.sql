-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3306
-- Generation Time: Apr 06, 2023 at 08:38 PM
-- Server version: 5.7.36
-- PHP Version: 7.4.26

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `api_gateway`
--

DELIMITER $$
--
-- Procedures
--
DROP PROCEDURE IF EXISTS `getCompanies`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `getCompanies` ()  BEGIN  
    SELECT * FROM companies;  
END$$

DROP PROCEDURE IF EXISTS `getCompanyByUsername`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `getCompanyByUsername` (IN `name` VARCHAR(255))  BEGIN  
    SELECT * FROM companies WHERE username = name;   
END$$

DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `apis`
--

DROP TABLE IF EXISTS `apis`;
CREATE TABLE IF NOT EXISTS `apis` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `api_url` varchar(255) DEFAULT NULL,
  `created_date` datetime(6) DEFAULT NULL,
  `description` longtext,
  `headers` longtext,
  `method` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `remove_date` datetime(6) DEFAULT NULL,
  `remove_status` bit(1) DEFAULT NULL,
  `request` longtext,
  `status` bit(1) DEFAULT NULL,
  `updated_date` datetime(6) DEFAULT NULL,
  `projects_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKan4fup48nmrfpd5i2q1r8atj0` (`projects_id`)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `apis`
--

INSERT INTO `apis` (`id`, `api_url`, `created_date`, `description`, `headers`, `method`, `name`, `remove_date`, `remove_status`, `request`, `status`, `updated_date`, `projects_id`) VALUES
(37, 'QfYOarllw1sghl/W47tG44dP4vdowIC/BF4UZ3fJ6NAFQDH+uRaBAP3K0caOKgjoIZ+3xfOhoA==', '2023-04-04 02:15:48.769000', 'Zc09U81qrWcGIHhq7d+gjiJ5/ouaYt5eng==', 'UqU5de0+iVk7yGrD+7ESrZoXorc0hoKuGRgYZjXT+tESSCDTVVK8Jh4ULSjJ1EblvvAz', 'We0JbtRR3jjIaHjn+Wgj0J04jP8=', 'Re0dc+0tueMxHrnDFuMW67MCprkA', NULL, NULL, 'UqUPaeY4glYigBmArPMZsM0XobQvgJOrSktQL2fwMi6yPilam8yXZfMkbbYo', NULL, '2023-04-04 02:32:23.754000', 9);

-- --------------------------------------------------------

--
-- Table structure for table `api_security`
--

DROP TABLE IF EXISTS `api_security`;
CREATE TABLE IF NOT EXISTS `api_security` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `value` varchar(255) DEFAULT NULL,
  `apis_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK7bv3615tomsolltp7s0hx9cp4` (`apis_id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `api_security`
--

INSERT INTO `api_security` (`id`, `name`, `value`, `apis_id`) VALUES
(12, 'A8gT14LXJHHq8AsnfLefDFbPBGhu5Pup96nQpVCExmTFbWy5D8YQgqICvvCrFzy6eqU4VcEM7Jq+WnbZm922ZEiXHZX+kwswj8jkuwIda7RnycCiwVsnHoXNdaq5npwcTsBpb3OxjhknVzs5cfOjAU0LbWkBvjw1zM4LkXmrGZY=', 'jbAzSn8Gx3egqF5D2MJnrGV35PnKiDTAtYbmLRrILe0yxsTkCtWIU/tuUMuA4RKpWxm2+aBFxxqORBSBsc3nDvyOi6UUXBSkOVOFw3DRjGjLYcPl7ivWczJQuCUWDfI5S0/544i90kR+Q6J2zgibA54RhQBL6ciYQqNu0VmbRAU=', 37),
(13, 'T60TtZRWssbHhwlIcHrTdI/poeS4cYAtk4/RSRkAa1YivTEpIebBidjpSnAu7diiWbwq/AEfmbSGd3AUmlrcQhF7j/zGHDFnl8C5mkqXgtehHi7MoAR9q9VPPBiC1+3pAWTMz2vNRzNOD+1yJSImrjfNq79vLG1NGBpkvxYbQ4I=', 'EG+RbTQVmF87ADnwYFabFuZkQCgBPJvzBEdCMJwmecP5wOTjndHf+sbK9GeJ7T06KVfb3SD9vx8F9T3ObIOZ2Z0fSMU1MbsTd7hU4V9te3FyYWnP/e2xzEElifgPZ+ql2SvTDqbpI3SqxPMyTxm7AIxArZceCqbc5RCB7322T4w=', 37);

-- --------------------------------------------------------

--
-- Table structure for table `api_test_record`
--

DROP TABLE IF EXISTS `api_test_record`;
CREATE TABLE IF NOT EXISTS `api_test_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `remove_status` bit(1) DEFAULT NULL,
  `response` longtext,
  `test_date` datetime(6) DEFAULT NULL,
  `apis_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK4oklecas5ofy8tx3sdncmrxwh` (`apis_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `companies`
--

DROP TABLE IF EXISTS `companies`;
CREATE TABLE IF NOT EXISTS `companies` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `contact_number` varchar(255) DEFAULT NULL,
  `created_date` datetime(6) DEFAULT NULL,
  `email_id` varchar(255) DEFAULT NULL,
  `last_login_date` datetime(6) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `updated_date` datetime(6) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `companies`
--

INSERT INTO `companies` (`id`, `contact_number`, `created_date`, `email_id`, `last_login_date`, `name`, `password`, `updated_date`, `username`) VALUES
(1, 'OBUaWMfnqeuhYTE0MPxynVyCSyg6hXV6jLqlhnSJt4zBZb8vfY/E9crHiG28OAUI7c6Rnm8ZRoeGFzvBMn6t51pKPt1o8bWVZVzxI8iJiaPqt8D6l4YSbckMuqlSGE+f1DbIsBFEjRbMFk3P0kJetBoJkVoa3qXQgyA6DRAWniI=', '2023-03-29 18:28:12.027000', 'HSqUSDRCIyemvc9st1JjJBaBTR3MfaLzXuv8y323EErsz8aenseyQ5M0rsjBC83vrAXjdRobSEaOG75moPGMJRIW5UiVFqup4kHoi4nyziGT5vzhH6S5WACo7cFUwy+xxARk4T3KwcOplAg5gaWnlJSLLjdkqNNiNhlEw1f6BaA=', '2023-04-06 21:16:04.243000', 'VKqlS4oyYIGXmgC+BqXHS25g/q2LCUyactDYOx6AcjwmxSorqyVjHMTU9HjpU2YUHg/p9ZEcYbZHaT7MEwk42rjqiUf3jpWgWuKKAZFxYcQ0yU8uv7B4USK8T1oRYsMxu48Bgfek4IkarPOIVZYkTi+kVhqkIPNTavdg23ijmFQ=', 'iXOjRWOt6xs+bqRsRrK8X8zS7vRmS8Nm3AY+8ixuefocXWOagVTED76xf6ogNTsiQU9G2ZwYDqGHE1PtdelaHE3sb19lxLVjgd328fuag8kmtkZ4LKNo/G9YSyh6GHG0CpRvJn1Eepx2jo9f7F5HES3C/m/Ep5dVru3H46RyQ1Y=', '2023-03-29 18:28:12.027000', 'sam'),
(2, 'mKpINzU2khAy92xR9L5JnAYpO5qVnVB3sBy0J13xWolsDNBUYQMz1x9rNbY3jtVavf6FECh7EYvwd2qCf7KERAV1CgQ/yKEKFmuY+QVLR2PjsQVjR1j0hRNRbN6mL/NnpckLadt39p6BFxsJFAYgvrJYsUY0u7UXynaZhu/CmJc=', '2023-03-29 23:35:54.631000', 'EA2zAsB2BoTN8Rx8ylGIoEEKeKYmHEdwIzi13OzEtWnPs2omz6oTMpS7rHogJzxIkT0tXG8JH6NCAGCB9Zu/HLRmNbIAGKU9jI5f4qZW57/1Bpcay5ZZkTNat+9eE+bg30BoS9A8RwXpGOaYY/BHOe4/zz8DCj4QGymxb+TRfEU=', '2023-03-30 19:52:01.131000', 'VQ1D7IGsReE7QKI4Pqmgh2PddeghFN46WIVn5nzkC1L+bAiY8uFnDQHvis/1k/0kV8e4/BPfOKtwxIOTzLVUExUa8OPYu4xW4cMu+DrkYXu8UVzfoPAX74IhafJjpE23yvtMZeA/KtXxkAEmO8KNX98ZVh2fajgEn07dh1x27Pw=', 'eutC96OAqKm8Fb3CFu/rW1mPtns3Sw534LBqU8fj+0zKXguxBcoEUhpoW482kgX4sr5rhVhpwRdkop0rlDkyQT9k9OuNZ88mejepeKk+cuEAeyBx3xUtQs+Jyp1w5pZ0MPr2aPzXFbQiDcTaR6oveE79tx6y55YdYSPZ/Zm9NFs=', '2023-03-29 23:35:54.631000', 'PRUTHVI'),
(4, 'H4uxDH26xwIlfhr/RVAqpmpZTIjuhuoffP4tpZETNtR+0OWG5rOPiUqNd1Q9C34hFeXUt94evO/ODf0bkrH3E5xgFCw2uzhl1+al2sRd8VFEe6JeW47jEnOwkcf7DxQJIY2lWe0gGBzsgNHeCEeCsmRSYIsHGW6wPiAwNBJAN64=', '2023-04-07 01:44:01.804000', 'OVH2iVMwU6nyvY4j6PlGjiLwugdxrchK+7KciKOIju0GshunMFO44xdEOKRPzhoy73tg4wpYpKcFG6PMWliHTOl3ALn1kHDNgTaPufrDx7bYmabI0FdVzdJHhzizgICDBLCiYJfSgnx8jzF+oYrGJHihxUY+HPU1XKtMnQeZ6Nc=', '2023-04-07 01:44:01.804000', 'lLPdJemPy4N/ujzUWbpr2ux/203zQFkzPk88CxAKFcpd49v37TxqDHKDPSQXw1i10PthvlPC7hEPqlY354YkIZAFUehtpAkJ+5IwHlqkvry/Z5u+yyJs2iCR5krIerfXbKfGEl5o4PTa4f0P1cvh/f9lrmRGZfsAgDRyUI1CSy0=', 'd/lED5KLgvybXNN+6qBKeHDIk1rVzLcoG74TEXb//YpkISgCn8YfaMl8zGGEdNaBOGwX0tN3RtnKySyBxbDDDq2MmNdwR3Hfo1gw3jqU/4cH4Iy8yM6ilyhEoTZIj9gbqNAvNavJVAo2BY66TIfAmIIN6jQuWJjI0BrX59LWZh0=', '2023-04-07 01:44:01.804000', 'xyz'),
(5, 'dWlIEQ5PVU25saGz1/wikDtdykULVOsV5eriSzxzkRu8o9kmIGbfGkmPIkLaRUQWQkrrbuEozKyfMexW5lJiPLNk2n16oqqPIu/Bu9oNDS3+4LfnDmIFhBfekhB0xOPS6AB0ke7gtTBc114WfYtpTTrECFLpriCLCs1w8GLIOHM=', '2023-04-07 01:47:09.890000', 'rxA8yj07zG43n8bT7+WP9KbbvQwkjwdDG9RQJCtnCIFB1gbVU2Bh6pZh+eJ4p07JcfobuA4GsNVQZi8hma8AdwbdOj0hS4AiLWGVvLp2jZy+rx3TyeqMhWgPLflXluDPlb/vIBy0a2FP1lWOEys/+3uvy/sbyujZG6ktAKivdz0=', '2023-04-07 01:47:09.890000', 'aHIylWiGO/XbLcaXx5+YFnDlIKepY6AJ1pLnuCGTzLEjP3yBHMKpJNJ6JN8Te2LwksOzLmZbwMth3CwqsnWafmGgvL/9v1VkB/iTPfudo73WyCWF49Oz6DW2fPXEXnkrMa9yIE8+ggv2JGB/ZLrdsecnIx+4vlKlnPBX5CfscFg=', 'OI5+zAeIW1JiqbqnClB46T0sTLiMa1Fn3eVDm4a7sAjn4jx1MnXz9GNOBjksgjZdWMzM7J8w1UiMzK893GYoKB++Y50y16uhcShyAbSvvE2MyzDVNwhzjsDDjDJ+WUghGGkx2b+KJ+lSCFIM4QXwUcHVpsRt9Ubz9deYVFg9XNE=', '2023-04-07 01:47:09.890000', 'ZXY');

-- --------------------------------------------------------

--
-- Table structure for table `logs`
--

DROP TABLE IF EXISTS `logs`;
CREATE TABLE IF NOT EXISTS `logs` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `header` longtext,
  `log_date` datetime(6) DEFAULT NULL,
  `remove_status` bit(1) DEFAULT NULL,
  `request_body` longtext,
  `response` longtext,
  `apis_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKoktovmq9ebc8qh58cb8gk7isf` (`apis_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `logs`
--

INSERT INTO `logs` (`id`, `header`, `log_date`, `remove_status`, `request_body`, `response`, `apis_id`) VALUES
(1, 'UsEVdPcvgkNisUfK7ulu9s0Gvq47jpWmAh9YYmnW5+MBJoeHzH24UOehBWFxvdmo0g==', '2023-04-06 21:05:00.427000', NULL, 'UqAPaeY4glYigByAqadU+p9a8Lc5nJK4AgMTKiCb+t8RTSC3+0sdFNvbxGKM4MXByQaf', 'UqAIf/A6g1k8gByA8PZW+NAGs6kho46oBB8lbWnJ5tAPCn+rpV2OxcK4MV5rpHf4Uv4+wRryouoqvHf8d8bmW3QBv1Dd4WF1wzN28akQSDyTbuosCf6Gs074cVeox8MdhZVciWvnuSRGQQrGi09m0izTtAZnZGy5egkms2SWvd1rKcbiihkM76bzsIBAXuHU5oZF12K+qDkjylLIOltHpzzDVqjMcBXk75BDVyIa42JwubzoXfr5Cq/PpRLsXs7/1JFSx01O2SJ80guuQ0rPZk47McAUVJAAMbhnUITPiiooud0rXfrl3VcDdyt+ht6aaNKz5nVmM4pucdi30q4vltRjZ3NkycNhShlEU8jwUWWvDQ1h7kZJMFsHOTn+WOPQstxqxztgoUbpUGwkL+YKpLXE3l2PfUsJMBCki5hbMJDya3rN2hsO7YcwZXpaBikcAPb0za+wk0P53LDLArqCXFoipk9n4SDpTBtqLQ6240EoaU6N0mOuHGI72ZE3+I+UmdlHZ1SPSMBV/jC15FJbtIJlooY//uboZbvd4tMMNV0jdCXOY4hlTt+z8lLKnHMXEZBHfohPfzyyFbDU/crK0mypuX8Fm2CdxDRzRq7Cz/v/T+EWQY1YBCCjS4hRpLBF0T32dvuIqXeqkr+dtV30VMh9V07OZcwAsPTTJ1yayJQiEv++tP7h8BT+aTm1GhA7xVOJNrujXMUEAVNML3zhGAszIL9CnaMGjrwSUwr2Hz1zM8XOs1kWH0BWwBonQ04Zu9FllQDfmwTm0fZA2In5utdBYM4ogcLCu/HpjKe1ORD+qLpTDE7pWn6c4Ktu3xph/C7Z7TA+yHI/D1/H9bO0w9zxxnmNa6v/P5jYWJqL+P+8AnA8RBwwtm+h7pBQA3HJfG0q52lBx4ljW4gx7XRLsTCE7QXT4GglKEJwk8g5I6fGBe0tl+cK2PVy9RxkJxboXn2b1fT5kAHSJi2+dZ94IFgLBZfEMIuiOvymQyDbzK/moKgzFPemXSClvo8GsAjb8RukvFZSYWKs8K7OHKPBQQq0CfKqLxtZytObpYXUSh+P25eErowPd0bxvidU1+WqDPN3c3c4ZkjRGnepHd/8/v4pXIHB9d4P2sWl4Bk6L8+NJXZJH7M+q+AXfuDDYeNJCPYhCbBvbBzHoSmzrdSdlYfiAJyHIpJpA8NDM1RaNWvuudcCdGsrWldNOzEy/ck9YNbsTv/OayDYA4aepm4nqMTWIyne3OMrIE+DSzVTiArvwp/cGtEjy5vBFvpthTv+WFhFgKUFJsTMVSlOI2593SSl+qmF+xzBgNt1cAgC57jaZ9A4w20MqFF9Buwwnu2ceVYbHf0CDbUMo7sIElPtxeg2uqxBXLU8lV3uoEIrVUpgX4jB4wuKEa4ApnRwLA==', 37);

-- --------------------------------------------------------

--
-- Table structure for table `projects`
--

DROP TABLE IF EXISTS `projects`;
CREATE TABLE IF NOT EXISTS `projects` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_date` datetime(6) DEFAULT NULL,
  `information` longtext,
  `name` varchar(255) DEFAULT NULL,
  `status` bit(1) DEFAULT NULL,
  `updated_date` datetime(6) DEFAULT NULL,
  `companies_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKefjoaanee4dnahynd7itourt3` (`companies_id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `projects`
--

INSERT INTO `projects` (`id`, `created_date`, `information`, `name`, `status`, `updated_date`, `companies_id`) VALUES
(9, '2023-03-29 23:34:15.853000', 'SAM (Siddhatech Api Manager)', 'SAM', b'1', '2023-03-30 19:59:53.568000', 1),
(12, '2023-03-31 00:57:44.082000', 'SAM (Siddhatech Api Manager)', 'ABC', b'1', '2023-03-31 01:36:34.018000', 1);

-- --------------------------------------------------------

--
-- Table structure for table `project_setting`
--

DROP TABLE IF EXISTS `project_setting`;
CREATE TABLE IF NOT EXISTS `project_setting` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_date` datetime(6) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `updated_date` datetime(6) DEFAULT NULL,
  `value` longtext,
  `projects_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKe7m3asoumduwo79pxi4uhlgcu` (`projects_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `proxy_apis`
--

DROP TABLE IF EXISTS `proxy_apis`;
CREATE TABLE IF NOT EXISTS `proxy_apis` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `generatedAt` datetime DEFAULT NULL,
  `headers` varchar(255) DEFAULT NULL,
  `method` varchar(255) DEFAULT NULL,
  `request` varchar(255) DEFAULT NULL,
  `status` bit(1) NOT NULL,
  `url` varchar(255) DEFAULT NULL,
  `apis_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKsdfg2aeatfekxy3vaudm2avxk` (`apis_id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `proxy_apis`
--

INSERT INTO `proxy_apis` (`id`, `generatedAt`, `headers`, `method`, `request`, `status`, `url`, `apis_id`) VALUES
(24, '2023-04-04 02:15:49', 'UqU5de0+iVk7yGrD+7ESrZoXorc0hoKuGRgYZjXT+tESSCDTVVK8Jh4ULSjJ1EblvvAz', 'We0JbtRR3jjIaHjn+Wgj0J04jP8=', 'UvIbY+8ljVN1nhnP+LFH+dwbt+BiyMbjSgEWe2nO5swYSGe2+QKSkVDeWibX5b718nFdQQozsQ==', b'1', 'QfYOarllw1sghl/W47tG44dP4vdowJKuAF4EaXeW5dEbBjPeaXVnzoNTkFbp0JlsNpeo', 37);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `apis`
--
ALTER TABLE `apis`
  ADD CONSTRAINT `FKan4fup48nmrfpd5i2q1r8atj0` FOREIGN KEY (`projects_id`) REFERENCES `projects` (`id`);

--
-- Constraints for table `api_security`
--
ALTER TABLE `api_security`
  ADD CONSTRAINT `FK7bv3615tomsolltp7s0hx9cp4` FOREIGN KEY (`apis_id`) REFERENCES `apis` (`id`);

--
-- Constraints for table `api_test_record`
--
ALTER TABLE `api_test_record`
  ADD CONSTRAINT `FK4oklecas5ofy8tx3sdncmrxwh` FOREIGN KEY (`apis_id`) REFERENCES `apis` (`id`);

--
-- Constraints for table `logs`
--
ALTER TABLE `logs`
  ADD CONSTRAINT `FKoktovmq9ebc8qh58cb8gk7isf` FOREIGN KEY (`apis_id`) REFERENCES `apis` (`id`);

--
-- Constraints for table `projects`
--
ALTER TABLE `projects`
  ADD CONSTRAINT `FKefjoaanee4dnahynd7itourt3` FOREIGN KEY (`companies_id`) REFERENCES `companies` (`id`);

--
-- Constraints for table `project_setting`
--
ALTER TABLE `project_setting`
  ADD CONSTRAINT `FKe7m3asoumduwo79pxi4uhlgcu` FOREIGN KEY (`projects_id`) REFERENCES `projects` (`id`);

--
-- Constraints for table `proxy_apis`
--
ALTER TABLE `proxy_apis`
  ADD CONSTRAINT `FKsdfg2aeatfekxy3vaudm2avxk` FOREIGN KEY (`apis_id`) REFERENCES `apis` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
