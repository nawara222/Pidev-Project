-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Mar 04, 2024 at 01:11 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `pidevvinci`
--

-- --------------------------------------------------------

--
-- Table structure for table `art`
--

CREATE TABLE `art` (
  `id_art` int(11) NOT NULL,
  `title` varchar(255) NOT NULL,
  `materials` varchar(255) NOT NULL,
  `height` double NOT NULL,
  `width` double NOT NULL,
  `type` varchar(255) NOT NULL,
  `city` varchar(255) NOT NULL,
  `description` varchar(255) NOT NULL,
  `price` float NOT NULL,
  `id_category` int(11) NOT NULL,
  `path_image` varchar(255) NOT NULL,
  `dateCreation` datetime NOT NULL DEFAULT current_timestamp(),
  `video` varchar(255) NOT NULL,
  `Userid` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `art`
--

INSERT INTO `art` (`id_art`, `title`, `materials`, `height`, `width`, `type`, `city`, `description`, `price`, `id_category`, `path_image`, `dateCreation`, `video`, `Userid`) VALUES
(17, 'mehdi', 'rar', 144, 1555, 'RATRR', 'TN', '1FDDF', 1555, 6, 'C:\\xampp\\htdocs\\image\\auction.png', '2024-03-02 16:00:01', 'C:\\xampp\\htdocs\\image\\Nature Beautiful short video 720p HD.mp4', 1),
(18, 'MehdiARTZEZZ', 'FINE LEATHER', 1500, 9000, 'RARE', 'TUNISIA', 'BOMBOM', 999, 6, 'C:\\xampp\\htdocs\\image\\image-1.png', '2024-03-03 13:28:58', 'C:\\xampp\\htdocs\\image\\Nature Beautiful short video 720p HD.mp4', 36),
(19, 'Malek', 'LEATHER', 180, 190, 'Rare', 'Tunisia', 'it\'s so good', 70, 6, 'C:\\xampp\\htdocs\\image\\art.png', '2024-03-03 13:48:19', 'C:\\xampp\\htdocs\\image\\Nature Beautiful short video 720p HD.mp4', 36),
(20, 'mehdizaiem', 'TTTT', 144, 144, 'RARE', 'TN', 'FFF', 555, 6, 'C:\\xampp\\htdocs\\image\\art.png', '2024-03-03 21:07:18', 'C:\\xampp\\htdocs\\image\\Nature Beautiful short video 720p HD.mp4', 36);

-- --------------------------------------------------------

--
-- Table structure for table `auction`
--

CREATE TABLE `auction` (
  `id` int(11) NOT NULL,
  `Auctionname` varchar(20) NOT NULL,
  `price` int(255) NOT NULL,
  `date` varchar(50) NOT NULL,
  `time` varchar(50) NOT NULL,
  `description` text DEFAULT NULL,
  `imgpath` varchar(250) DEFAULT NULL,
  `Userid` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `auction`
--

INSERT INTO `auction` (`id`, `Auctionname`, `price`, `date`, `time`, `description`, `imgpath`, `Userid`) VALUES
(3, 'plswork', 140, '2024-03-09', '993', '&\'dsd', 'file:/C:/xampp/htdocs/image/image-3.png', 1),
(4, 'HAMZOUZ', 434, '2024-03-03', '14:00', 'DDD', 'file:/C:/xampp/htdocs/image/logo.png', 36),
(5, 'PLSWORKS', 14, '2024-03-06', '44', '444', 'file:/C:/xampp/htdocs/image/image-1.png', 1),
(7, 'MEHDIARTS', 1414, '2025-03-14', '14:14', 'domdom', 'file:/C:/xampp/htdocs/image/image-1.png', 36),
(8, 'Mehdi', 12, '2024-03-13', '19:00', 'damdamn', 'file:/C:/xampp/htdocs/image/image-5.png', 36),
(11, 'AZERTY', 233, '2024-03-29', '14:00', 'B3AW', 'file:/C:/xampp/htdocs/image/4048564.jpg', 27),
(12, 'chabachi', 44, '2024-03-08', '14:00', 'TTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT', 'file:/C:/xampp/htdocs/image/minimalist-style-hand-painted-liquid-background-free-vector.jpg', 36);

-- --------------------------------------------------------

--
-- Table structure for table `basket`
--

CREATE TABLE `basket` (
  `idB` int(11) NOT NULL,
  `quantity` int(11) NOT NULL,
  `total_price` float NOT NULL,
  `Userid` int(11) NOT NULL,
  `id_art` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `basket`
--

INSERT INTO `basket` (`idB`, `quantity`, `total_price`, `Userid`, `id_art`) VALUES
(7, 1, 1244, 36, 17),
(8, 1, 1244, 36, 17);

-- --------------------------------------------------------

--
-- Table structure for table `bid`
--

CREATE TABLE `bid` (
  `idbid` int(11) NOT NULL,
  `bidamount` int(11) NOT NULL,
  `idAuction` int(11) NOT NULL,
  `Userid` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `bid`
--

INSERT INTO `bid` (`idbid`, `bidamount`, `idAuction`, `Userid`) VALUES
(1, 1444, 3, 1),
(2, 1445, 3, 31),
(3, 1446, 3, 31),
(4, 4444, 4, 36),
(5, 444, 5, 1),
(6, 1415, 7, 36),
(7, 1447, 3, 36),
(8, 1448, 3, 27),
(9, 1416, 7, 36),
(10, 1500, 3, 36);

-- --------------------------------------------------------

--
-- Table structure for table `category`
--

CREATE TABLE `category` (
  `id_category` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `date` varchar(255) NOT NULL,
  `Userid` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `category`
--

INSERT INTO `category` (`id_category`, `name`, `date`, `Userid`) VALUES
(6, 'MEHDI', '2024-03-16', 1);

-- --------------------------------------------------------

--
-- Table structure for table `courses`
--

CREATE TABLE `courses` (
  `id_C` int(11) NOT NULL,
  `nameC` varchar(30) NOT NULL,
  `descriptionC` varchar(255) NOT NULL,
  `priceC` float NOT NULL,
  `type` varchar(30) NOT NULL,
  `numberW` int(11) DEFAULT NULL,
  `image_path` varchar(255) NOT NULL,
  `Userid` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `courses`
--

INSERT INTO `courses` (`id_C`, `nameC`, `descriptionC`, `priceC`, `type`, `numberW`, `image_path`, `Userid`) VALUES
(1, 'MEEehdi', '8ALI BARCHAaaaaaaa', 1999, 'PAint', 1, 'file:/C:/xampp/htdocs/image/auction.png', 36),
(3, 'BA3AW', 'B3AWWWWWWWWWWWWss', 9999, 'PAINT', 2, 'file:/C:/xampp/htdocs/image/logo.png', 36);

-- --------------------------------------------------------

--
-- Table structure for table `event`
--

CREATE TABLE `event` (
  `idE` int(11) NOT NULL,
  `nameE` varchar(255) NOT NULL,
  `dateE` date NOT NULL,
  `durationE` int(11) NOT NULL,
  `typeE` varchar(255) NOT NULL,
  `entryFeeE` double NOT NULL,
  `capacityE` int(11) NOT NULL,
  `Userid` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `event`
--

INSERT INTO `event` (`idE`, `nameE`, `dateE`, `durationE`, `typeE`, `entryFeeE`, `capacityE`, `Userid`) VALUES
(2, 'MEHDIZZZ', '2024-03-13', 20, 'ARTS', 22, 22, 36);

-- --------------------------------------------------------

--
-- Table structure for table `feedback`
--

CREATE TABLE `feedback` (
  `feedback_id` int(11) NOT NULL,
  `status` varchar(255) NOT NULL,
  `type` varchar(255) NOT NULL,
  `question` text NOT NULL,
  `answer` text DEFAULT NULL,
  `user_satisfaction` varchar(255) DEFAULT NULL,
  `id_U` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `feedback`
--

INSERT INTO `feedback` (`feedback_id`, `status`, `type`, `question`, `answer`, `user_satisfaction`, `id_U`) VALUES
(4, 'Closed', 'Feature Request', 'hello!', 'hi!', '', 27),
(5, 'Closed', 'Feature Request', 's', 'n', '', 27),
(6, 'Open', 'Feature Request', 'ss', '', '', 27),
(7, 'Open', 'Bug Report', 'ss', '', '', 27),
(8, 'Open', 'General Feedback', 'ss', '', '', 27);

-- --------------------------------------------------------

--
-- Table structure for table `order`
--

CREATE TABLE `order` (
  `idO` int(11) NOT NULL,
  `totalP` float NOT NULL,
  `dateC` varchar(255) NOT NULL,
  `status` varchar(255) NOT NULL,
  `idB` int(11) NOT NULL,
  `Userid` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `ticket`
--

CREATE TABLE `ticket` (
  `idT` int(11) NOT NULL,
  `idE` int(11) NOT NULL,
  `qrCodeT` varchar(255) NOT NULL,
  `idU` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `ticket`
--

INSERT INTO `ticket` (`idT`, `idE`, `qrCodeT`, `idU`) VALUES
(2, 2, 'C:\\xampp\\htdocs\\QRcode\\QRCode_EventID_2_UserID_1.png', 1);

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `user_id` int(11) NOT NULL,
  `firstname` varchar(255) NOT NULL,
  `lastname` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `email_address` varchar(255) NOT NULL,
  `role` varchar(255) NOT NULL,
  `account_status` varchar(255) NOT NULL,
  `date_created` datetime NOT NULL DEFAULT current_timestamp(),
  `last_login` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`user_id`, `firstname`, `lastname`, `password`, `email_address`, `role`, `account_status`, `date_created`, `last_login`) VALUES
(1, 'Admin', 'UserAdmin', '$2a$10$emmlw7tGQSVzjZ2aiyALDOFDXvHMMz61UgMAukOJ4jo5OxVS5sHIK', 'Admin@admin.com', 'UserAdmin', 'Active', '2024-02-17 01:37:38', '2024-03-03 22:05:36'),
(15, 'modified', 'chaieb', '$2a$10$4W0X2Yl5EIro2tM1rhq4Pe4V6RT6/8681K/jz4c.p2hJqaGaXlzbG', 'hamza.chaieb1@esprit.tn', 'Amateur', 'Disabled', '2024-02-17 19:06:35', '2024-02-18 23:58:52'),
(27, 'addedbysuser', 'addedbysuser', '$2a$10$XrbWaTwuUiaeMGvZNxB8vO0fjl3OD2YDN0vRMjSbrRd1VgQhxKfP.', 'addedbysuser@esprit.tn', 'Artist', 'Active', '2024-02-24 00:17:24', '2024-03-03 23:48:54'),
(28, 'testing', 'create', '$2a$10$76crDU9VRMATTRHAbfOYc.r8aThMhk5xTN9afZBA9J/tVIBviTk1O', 'creat@esprit.tn', 'Amateur', 'Active', '2024-02-25 00:46:59', NULL),
(29, 'hamza', 'hamza', '$2a$10$Pd4V5sppG65S3zUuFZiapuiEXUfe4w6PEn/WpdhMcdJLwG2/861lC', 'hash@esprit.tn', 'Artist', 'Active', '2024-02-25 03:00:52', '2024-02-25 03:29:43'),
(30, 'hamza', 'hamza', '$2a$10$kEOhUzJAYPDw/RQeMTYiKOtCXDGXWE5PaeOPrcIk7kLek9q6CMlY6', 'hitthetarget735@gmail.com', 'Artist', 'Active', '2024-02-25 03:00:52', '2024-02-29 21:51:29'),
(31, 'hamza', 'chaieb', '$2a$10$uRHcMFwsprgPGMyUgySZQeFnZjFt0mo1LKXU9rbcuLcPQSnxCRRvu', 'hamza.chaieb@esprit.tn', 'Amateur', 'Active', '2024-02-28 02:59:21', '2024-03-03 22:10:59'),
(32, 'x', 'y', '$2a$10$bt8Sv03tOiiFZohwg8OwNO7irkkUkS3lh6t.XJzixYubSbiZWsjNq', 'x@esprit.tn', 'Amateur', 'Active', '2024-02-28 23:41:37', '2024-03-03 22:39:13'),
(33, 'x', 'x', '$2a$10$Jp5hDF42BxATVARnqW3N2eRUgGJPVav5Uw/em35HtsSdZc4iLnL4G', 'xy@esprit.tn', 'Amateur', 'Active', '2024-02-29 00:12:03', '2024-03-04 00:03:14'),
(34, 'Admin', 'UserAdmin', '$2a$10$emmlw7tGQSVzjZ2aiyALDOFDXvHMMz61UgMAukOJ4jo5OxVS5sHIK', 'Adminart@admin.com', 'ArtAdmin', 'Active', '2024-02-17 01:37:38', '2024-03-02 00:21:56'),
(36, 'Mehdi', 'Zaiem', '$2a$10$7WwuyBi0EXhbUakBz/dkBO6ZUmeyHUYGHUecyHMSyQeGVPwCqoNY.', 'Mehdizaiem@esprit.tn', 'Artist', 'Active', '2024-03-01 23:01:45', '2024-03-04 01:04:51'),
(37, 'Admin', 'AuctionAdmin', '$2a$10$emmlw7tGQSVzjZ2aiyALDOFDXvHMMz61UgMAukOJ4jo5OxVS5sHIK', 'AdminAuction@admin.com', 'AuctionAdmin', 'Active', '2024-02-17 01:37:38', '2024-03-03 00:19:49');

-- --------------------------------------------------------

--
-- Table structure for table `workshops`
--

CREATE TABLE `workshops` (
  `id_W` int(11) NOT NULL,
  `nameW` varchar(255) NOT NULL,
  `resources` varchar(255) NOT NULL,
  `description` varchar(255) NOT NULL,
  `duration` float NOT NULL,
  `id_C` int(11) NOT NULL,
  `Userid` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `workshops`
--

INSERT INTO `workshops` (`id_W`, `nameW`, `resources`, `description`, `duration`, `id_C`, `Userid`) VALUES
(2, 'b3aw', 'dom', 'b3awwwwww', 9999, 3, 1),
(3, 'SAWSAW', 'SAWSAW', 'SAWSAW', 999, 3, 1),
(4, 'MEHDI', 'PAINT', 'bawbaw', 5, 1, 36);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `art`
--
ALTER TABLE `art`
  ADD PRIMARY KEY (`id_art`),
  ADD KEY `id_category` (`id_category`),
  ADD KEY `Userid` (`Userid`);

--
-- Indexes for table `auction`
--
ALTER TABLE `auction`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fkUserid` (`Userid`);

--
-- Indexes for table `basket`
--
ALTER TABLE `basket`
  ADD PRIMARY KEY (`idB`),
  ADD KEY `Userid` (`Userid`),
  ADD KEY `id_art` (`id_art`);

--
-- Indexes for table `bid`
--
ALTER TABLE `bid`
  ADD PRIMARY KEY (`idbid`),
  ADD KEY `fkidauction` (`idAuction`),
  ADD KEY `fkiduser` (`Userid`);

--
-- Indexes for table `category`
--
ALTER TABLE `category`
  ADD PRIMARY KEY (`id_category`),
  ADD KEY `Userid` (`Userid`);

--
-- Indexes for table `courses`
--
ALTER TABLE `courses`
  ADD PRIMARY KEY (`id_C`),
  ADD KEY `Userid` (`Userid`);

--
-- Indexes for table `event`
--
ALTER TABLE `event`
  ADD PRIMARY KEY (`idE`),
  ADD KEY `Userid` (`Userid`);

--
-- Indexes for table `feedback`
--
ALTER TABLE `feedback`
  ADD PRIMARY KEY (`feedback_id`),
  ADD KEY `id_U` (`id_U`);

--
-- Indexes for table `order`
--
ALTER TABLE `order`
  ADD PRIMARY KEY (`idO`),
  ADD KEY `fkidbasket` (`idB`),
  ADD KEY `Userid` (`Userid`);

--
-- Indexes for table `ticket`
--
ALTER TABLE `ticket`
  ADD PRIMARY KEY (`idT`),
  ADD KEY `idE` (`idE`),
  ADD KEY `idU` (`idU`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`user_id`),
  ADD UNIQUE KEY `email_address` (`email_address`);

--
-- Indexes for table `workshops`
--
ALTER TABLE `workshops`
  ADD PRIMARY KEY (`id_W`),
  ADD KEY `fkCoursesid` (`id_C`),
  ADD KEY `Userid` (`Userid`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `art`
--
ALTER TABLE `art`
  MODIFY `id_art` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;

--
-- AUTO_INCREMENT for table `auction`
--
ALTER TABLE `auction`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT for table `basket`
--
ALTER TABLE `basket`
  MODIFY `idB` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `bid`
--
ALTER TABLE `bid`
  MODIFY `idbid` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `category`
--
ALTER TABLE `category`
  MODIFY `id_category` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `courses`
--
ALTER TABLE `courses`
  MODIFY `id_C` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `event`
--
ALTER TABLE `event`
  MODIFY `idE` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `feedback`
--
ALTER TABLE `feedback`
  MODIFY `feedback_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `order`
--
ALTER TABLE `order`
  MODIFY `idO` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `ticket`
--
ALTER TABLE `ticket`
  MODIFY `idT` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=38;

--
-- AUTO_INCREMENT for table `workshops`
--
ALTER TABLE `workshops`
  MODIFY `id_W` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `art`
--
ALTER TABLE `art`
  ADD CONSTRAINT `art_ibfk_1` FOREIGN KEY (`Userid`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `cat_art` FOREIGN KEY (`id_category`) REFERENCES `category` (`id_category`);

--
-- Constraints for table `auction`
--
ALTER TABLE `auction`
  ADD CONSTRAINT `fkuserid` FOREIGN KEY (`Userid`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `basket`
--
ALTER TABLE `basket`
  ADD CONSTRAINT `basket_ibfk_1` FOREIGN KEY (`Userid`) REFERENCES `users` (`user_id`),
  ADD CONSTRAINT `basket_ibfk_2` FOREIGN KEY (`id_art`) REFERENCES `art` (`id_art`);

--
-- Constraints for table `bid`
--
ALTER TABLE `bid`
  ADD CONSTRAINT `fkidauction` FOREIGN KEY (`idAuction`) REFERENCES `auction` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fkiduser` FOREIGN KEY (`Userid`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `category`
--
ALTER TABLE `category`
  ADD CONSTRAINT `category_ibfk_1` FOREIGN KEY (`Userid`) REFERENCES `users` (`user_id`);

--
-- Constraints for table `courses`
--
ALTER TABLE `courses`
  ADD CONSTRAINT `courses_ibfk_1` FOREIGN KEY (`Userid`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `event`
--
ALTER TABLE `event`
  ADD CONSTRAINT `event_ibfk_1` FOREIGN KEY (`Userid`) REFERENCES `users` (`user_id`);

--
-- Constraints for table `feedback`
--
ALTER TABLE `feedback`
  ADD CONSTRAINT `feedback_ibfk_1` FOREIGN KEY (`id_U`) REFERENCES `users` (`user_id`);

--
-- Constraints for table `order`
--
ALTER TABLE `order`
  ADD CONSTRAINT `fkidbasket` FOREIGN KEY (`idB`) REFERENCES `basket` (`idB`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `order_ibfk_1` FOREIGN KEY (`Userid`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `ticket`
--
ALTER TABLE `ticket`
  ADD CONSTRAINT `ticket_ibfk_1` FOREIGN KEY (`idE`) REFERENCES `event` (`idE`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `ticket_ibfk_2` FOREIGN KEY (`idU`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `workshops`
--
ALTER TABLE `workshops`
  ADD CONSTRAINT `fkCoursesid` FOREIGN KEY (`id_C`) REFERENCES `courses` (`id_C`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `workshops_ibfk_1` FOREIGN KEY (`Userid`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
