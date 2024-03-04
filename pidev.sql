-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Feb 26, 2024 at 03:43 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `pidev`
--

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
(2, 'Closed', 'General Feedback', 'i love the application!', 'thank you!', NULL, 12),
(3, 'Closed', 'Bug Report', 'i had an error buying art , thank you !', 'we\'ll try to resolve that within 24 hours', '', 12),
(4, 'Closed', 'Feature Request', 'hello!', 'hi!', '', 27);

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
(12, 'hamzamodon', 'chaieb', '$2a$10$/2.uK.z5S.W28llhUw8sxOUgYt7UuMkIc6Q/.rD6qhB8L.rsvmDem', 'hamza.chaieb@esprit.tn', 'Amateur', 'Active', '2024-02-17 19:06:35', '2024-02-26 02:21:46'),
(15, 'modified', 'chaieb', '$2a$10$4W0X2Yl5EIro2tM1rhq4Pe4V6RT6/8681K/jz4c.p2hJqaGaXlzbG', 'hamza.chaieb1@esprit.tn', 'Amateur', 'Disabled', '2024-02-17 19:06:35', '2024-02-18 23:58:52'),
(22, 'Admin', 'UserAdmin', '$2a$10$emmlw7tGQSVzjZ2aiyALDOFDXvHMMz61UgMAukOJ4jo5OxVS5sHIK', 'Admin@admin.com', 'UserAdmin', 'Active', '2024-02-17 01:37:38', '2024-02-26 02:20:30'),
(27, 'addedbysuser', 'addedbysuser', '$2a$10$XrbWaTwuUiaeMGvZNxB8vO0fjl3OD2YDN0vRMjSbrRd1VgQhxKfP.', 'addedbysuser@esprit.tn', 'Artist', 'Active', '2024-02-24 00:17:24', '2024-02-25 01:46:02'),
(28, 'testing', 'create', '$2a$10$76crDU9VRMATTRHAbfOYc.r8aThMhk5xTN9afZBA9J/tVIBviTk1O', 'creat@esprit.tn', 'Amateur', 'Active', '2024-02-25 00:46:59', NULL),
(29, 'tryinghash', 'hash', '$2a$10$Pd4V5sppG65S3zUuFZiapuiEXUfe4w6PEn/WpdhMcdJLwG2/861lC', 'hash@esprit.tn', 'Artist', 'Active', '2024-02-25 03:00:52', '2024-02-25 03:29:43');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `feedback`
--
ALTER TABLE `feedback`
  ADD PRIMARY KEY (`feedback_id`),
  ADD KEY `id_U` (`id_U`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`user_id`),
  ADD UNIQUE KEY `email_address` (`email_address`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `feedback`
--
ALTER TABLE `feedback`
  MODIFY `feedback_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=30;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `feedback`
--
ALTER TABLE `feedback`
  ADD CONSTRAINT `feedback_ibfk_1` FOREIGN KEY (`id_U`) REFERENCES `users` (`user_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
