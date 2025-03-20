USE master
IF EXISTS(select * from sys.databases where name='PRJ-PROJECT-TEST')
BEGIN
	ALTER DATABASE [PRJ-PROJECT-TEST] SET SINGLE_USER WITH ROLLBACK IMMEDIATE
	DROP DATABASE [PRJ-PROJECT-TEST]
END

CREATE DATABASE [PRJ-PROJECT-TEST]
GO
USE [PRJ-PROJECT-TEST]
GO
CREATE TABLE tblResourceMap
(
	id varchar(30) PRIMARY KEY,
	systemPath varchar(50) NOT NULL
)

CREATE TABLE tblUser
(
	id int PRIMARY KEY IDENTITY(1, 1),
	email nvarchar(50) NOT NULL UNIQUE,
	username varchar(30) NOT NULL UNIQUE,
	phoneNumber varchar(12) not null UNIQUE,
	password varchar(50) NOT NULL,
	persistentCookie varchar(255),
	googleId varchar(255),	
	facebookId varchar(255),
	isAdmin bit DEFAULT 0,
	credit money,
	status bit NOT NULL DEFAULT 1,

	displayName nvarchar(50),
	profileStringResourceId varchar(30),
	bio nvarchar(255),

	CONSTRAINT fk_user_resourceId FOREIGN KEY (profileStringResourceId) REFERENCES tblResourceMap(id)
)

CREATE TABLE tblPreference
(
	id int PRIMARY KEY IDENTITY(1, 1),
	userId int NOT NULL,
	keyword varchar(30) NOT NULL,

	CONSTRAINT fk_pref_user FOREIGN KEY (userId) REFERENCES tblUser
)

CREATE TABLE tblUserPreference
(
	userId int,
	preference nvarchar(30),

	PRIMARY KEY (userId, preference),
	CONSTRAINT fk_userpref_user FOREIGN KEY (userId) REFERENCES tblUser(id)
)

CREATE TABLE tblChatBox
(
	id int PRIMARY KEY IDENTITY(1, 1),
	user1 int,
	user2 int,

	CONSTRAINT fk_chatbox_user1 FOREIGN KEY (user1) REFERENCES tblUser(id),
	CONSTRAINT fk_chatbox_user2 FOREIGN KEY (user2) REFERENCES tblUser(id)
)

CREATE TABLE tblChatBoxContent
(
	id int PRIMARY KEY IDENTITY(1, 1),
	chatBoxId int,
	message nvarchar(100) NOT NULL,
	time DATETIME NOT NULL DEFAULT GETDATE(),
	senderId int,

	CONSTRAINT fk_cbcontent_sender FOREIGN KEY (senderId) REFERENCES tblUser(id),
	CONSTRAINT fk_cbcontent_chatbox FOREIGN KEY (chatBoxId) REFERENCES tblChatBox(id)
)

CREATE TABLE tblPromotion
(
	id int PRIMARY KEY IDENTITY(1, 1),
	creatorId int,
	name nvarchar(30) NOT NULL,
	type bit DEFAULT 0, -- 0 is %, 1 is flat
	ofAdmin bit DEFAULT 0, 
	value int NOT NULL,
	creationDate DATE DEFAULT GETDATE(),
	expireDate DATE NOT NULL,
	status bit,

	CONSTRAINT fk_promo_creator FOREIGN KEY (creatorId) REFERENCES tblUser(id)
)

CREATE TABLE tblShop
(
	id int PRIMARY KEY IDENTITY(1, 1),
	ownerId int,
	name nvarchar(30) NOT NULL,
	address nvarchar(100),
	profileStringResourceId varchar(30),
	visible bit DEFAULT 0,

	CONSTRAINT fk_shop_resourceId FOREIGN KEY (profileStringResourceId) REFERENCES tblResourceMap(id),
	CONSTRAINT fk_shop_owner FOREIGN KEY (ownerId) REFERENCES tblUser(id)
)

CREATE TABLE tblCategory
(
	id int PRIMARY KEY IDENTITY(0, 1),
	name nvarchar(30),
	imageStringResourceId varchar(30),
	parent_id int

	CONSTRAINT fk_category_resourceId FOREIGN KEY (imageStringResourceId) REFERENCES tblResourceMap(id),
	CONSTRAINT fk_category_parentId FOREIGN KEY (parent_id) REFERENCES tblCategory(id)
)

CREATE TABLE tblVariation
(
	id int PRIMARY KEY IDENTITY(1, 1),
	categoryId int,
	name nvarchar(30),
	datatype varchar(10),
	unit varchar(10),

	CONSTRAINT fk_variation_category FOREIGN KEY (categoryId) REFERENCES tblCategory(id)
)

ALTER TABLE tblVariation
ADD status bit DEFAULT 1;

CREATE TABLE tblVariationValue
(
	id int PRIMARY KEY IDENTITY(1, 1),
	variationId int,
	value varchar(32),

	constraint fk_variationvalue_variation foreign key (variationId) references tblVariation(id)
)

ALTER TABLE tblVariationValue
ADD status bit;

CREATE TABLE tblProduct
(
	id int PRIMARY KEY IDENTITY(1, 1),
	shopId int,
	categoryId int,
	name nvarchar(50) NOT NULL,
	description nvarchar(255),
	availablePromotionId int,
	imageStringResourceId varchar(30),
	status bit,
	
	CONSTRAINT fk_product_resourceId FOREIGN KEY (imageStringResourceId) REFERENCES tblResourceMap(id),
	CONSTRAINT fK_product_shop FOREIGN KEY (shopId) REFERENCES tblShop(id),
	CONSTRAINT fk_product_category FOREIGN KEY (categoryId) REFERENCES tblCategory(id),
	CONSTRAINT fk_product_promo FOREIGN KEY (availablePromotionId) REFERENCES tblPromotion(id)
)

CREATE TABLE tblProductImage 
(
	id int PRIMARY KEY IDENTITY(1, 1),
	productId int,
	imageStringResourceId varchar(30),

	CONSTRAINT fk_productimage_resourceId FOREIGN KEY (imageStringResourceId) REFERENCES tblResourceMap(id),
	CONSTRAINT fk_productimage_product FOREIGN KEY (productId) REFERENCES tblProduct
)

create table tblProductItem
(
	id int PRIMARY KEY IDENTITY(1, 1),
	productId int,
	stock int,
	price money,

	constraint fk_productitem_product foreign key (productId) references tblProduct(id)
)

create table tblProductCustomization
(
	id int PRIMARY KEY IDENTITY(1, 1),
	productItemId int,
	variationValueId int,

	constraint fk_productcustomization_productitem foreign key (productItemId) references tblProductItem(id),
	constraint fk_productcustomization_variationvalue foreign key (variationValueId) references tblVariationValue(id)
)

create table tblReview
(
	id int PRIMARY KEY IDENTITY(1, 1),
	userId int,
	productId int,
	rate int,
	comment nvarchar(255),
	status bit,

	constraint fk_review_user FOREIGN KEY (userId) REFERENCES tblUser(id),
	constraint fk_review_product FOREIGN KEY (productId) REFERENCES tblProduct(id)
)

CREATE TABLE tblCart
(
	id int PRIMARY KEY IDENTITY(1, 1),
	userId int,

	CONSTRAINT fk_cart_user FOREIGN KEY (userId) REFERENCES tblUser(id)
)

ALTER TABLE tblCart
ADD status bit;


CREATE TABLE tblCartItem
(
	id int PRIMARY KEY IDENTITY(1, 1),
	cartId int,
	productItemId int,
	quantity int NOT NULL,
	status bit DEFAULT 0,

	-- CONSTRAINT pk_cartitem PRIMARY KEY (cartId, productCustomizationId),
	CONSTRAINT fk_cartitem_cart FOREIGN KEY (cartId) REFERENCES tblCart(id),
	CONSTRAINT fk_cartitem_productitem FOREIGN KEY (productItemId) REFERENCES tblProductItem(id)
)

CREATE TABLE tblPaymentMethod
(
	id int PRIMARY KEY IDENTITY(1, 1),
	name varchar(30)
)

CREATE TABLE tblOrder
(
	id int PRIMARY KEY IDENTITY(1, 1),
	userId int,
	paymentMethodId int,
	shippingAddress nvarchar(100),
	promotionId int,
	date datetime DEFAULT GETDATE(),
	finalPrice money,  --after promotion and iclude shipping cost
	status bit DEFAULT 0,


	CONSTRAINT fk_order_user FOREIGN KEY (userId) REFERENCES tblUser(id),
	CONSTRAINT fK_order_paymentMethod FOREIGN KEY (paymentMethodId) REFERENCES tblPaymentMethod(id),
	CONSTRAINT fk_order_promotion FOREIGN KEY (promotionId) REFERENCES tblPromotion(id)
)

CREATE TABLE tblOrderedItem
(
	id int PRIMARY KEY IDENTITY(1, 1),
	orderId int,
	productItemId int,
	orderStatus varchar(30),
	quantity int NOT NULL,
	totalPrice money,
	shippingCost money, --per shop per order

	CONSTRAINT fk_ordereditem_order FOREIGN KEY (orderId) REFERENCES tblOrder(id),
	CONSTRAINT fk_ordereditem_productitem FOREIGN KEY (productItemId) REFERENCES tblProductItem(id)
)

CREATE TABLE tblOnholdCredit
(
	id int PRIMARY KEY IDENTITY(1, 1),
	userId int,
	amount money NOT NULL,
	date date DEFAULT GETDATE(),
	claimDate date DEFAULT DATEADD(day, 7, GETDATE()),

	CONSTRAINT fk_onhold_user FOREIGN KEY (userId) REFERENCES tblUser(id)
)

CREATE TABLE tblNotification
(
	id int PRIMARY KEY IDENTITY(1, 1),
	userId int, -- can be null if the notification is global
	title nvarchar(30) NOT NULL,
	body nvarchar(255),
	isRead bit DEFAULT 0,

	CONSTRAINT fk_notification_user FOREIGN KEY (userId) REFERENCES tblUser
)

CREATE TABLE tblServerStatistics
(
	id int PRIMARY KEY IDENTITY(1, 1), -- stats wont be deleted
	day date NOT NULL,
	totalMoneyEarned int,
	userNum int,
	productNum int,
	shopNum int,
	promotionNum int,
	purchaseNum int,
	visitNum int,
	peakSessionNum int,
	averageResponseTime int, -- in milisec
	maxResponseTime int
)

CREATE TABLE tblShopStatistics
(
	id int PRIMARY KEY IDENTITY(1,1),
	shopId int,
	CONSTRAINT fk_shopstatistics_id FOREIGN KEY (shopId) references tblShop(id)
)

-- TF-IdF RELATED STUFF
CREATE TABLE tblBaseVector 
(
    keyword nvarchar(MAX)
);

CREATE TABLE tblVector (
    productId int PRIMARY KEY,
    vector nvarchar(MAX)
);

GO
CREATE PROCEDURE ComputeTFIdF
AS
BEGIN
	DELETE FROM tblVector;
	DELETE FROM tblBaseVector;

	INSERT INTO tblBaseVector (keyword)
	(
		SELECT DISTINCT TRIM(value) AS keyword 
		FROM tblProduct
		CROSS APPLY STRING_SPLIT(LOWER(tblProduct.name), ' ')
		WHERE LEN(value) > 0

		UNION

		SELECT DISTINCT TRIM(value) AS keyword
		FROM tblProduct
		CROSS APPLY STRING_SPLIT(LOWER(tblProduct.description), ' ')
		WHERE LEN(value) > 0
	);

	WITH tfbefore AS (
		SELECT 
			p.id, 
			TRIM(s.value) AS keyword, 
			COUNT(*) * 0.7 / NULLIF((SELECT COUNT(*) FROM STRING_SPLIT(LOWER(p.name), ' ')), 0) AS tf
		FROM tblProduct p
		CROSS APPLY STRING_SPLIT(LOWER(p.name), ' ') s
		GROUP BY p.id, s.value, p.name

		UNION ALL

		SELECT 
			p.id, 
			TRIM(s.value) AS keyword, 
			COUNT(*) * 0.3 / NULLIF((SELECT COUNT(*) FROM STRING_SPLIT(LOWER(p.description), ' ')), 0) AS tf
		FROM tblProduct p
		CROSS APPLY STRING_SPLIT(LOWER(p.description), ' ') s
		GROUP BY p.id, s.value, p.description
	),
	tf_combined AS (
		SELECT 
			id, 
			keyword, 
			SUM(tf) AS tf 
		FROM tfbefore
		GROUP BY id, keyword
	),
	tf AS (
		SELECT b.id, a.keyword AS keyword, COALESCE(b2.tf, 0) tf
		FROM (SELECT DISTINCT keyword FROM tblBaseVector) a
		CROSS JOIN (SELECT DISTINCT id FROM tf_combined) b
		LEFT JOIN tf_combined b2 ON b.id = b2.id AND a.keyword = b2.keyword
	),
	df AS (
		SELECT keyword, COUNT(DISTINCT id) AS DF FROM tf_combined GROUP BY keyword
	),
	tfidf AS (
		SELECT 
			tf.id, 
			tf.keyword, 
			tf.TF * LOG((SELECT COUNT(*) FROM tblProduct) / NULLIF(df.DF, 0)) AS TFIdF
		FROM tf
		JOIN df ON tf.keyword = df.keyword
	),
	vectors AS (
		SELECT 
			id, 
			STRING_AGG(CAST(COALESCE(tfidf, 0) AS NVARCHAR), ',') WITHIN GROUP (ORDER BY tblBaseVector.keyword) AS Vector
		FROM tblBaseVector
		LEFT JOIN tfidf ON tblBaseVector.keyword = tfidf.keyword AND tfidf.id = id  -- Fixed join condition
		GROUP BY id
	)
	INSERT INTO tblVector
	SELECT * FROM vectors;
END;
GO

GO
CREATE PROCEDURE GetRecommendation (@query NVARCHAR(400), @page int, @category int)
AS
BEGIN
	SET NOCOUNT ON;

	IF @query IS NULL OR LTRIM(RTRIM(@query)) = ''
    BEGIN
		WITH category AS (
        SELECT id FROM tblCategory WHERE id = @category
        UNION ALL
        SELECT c.id FROM tblCategory c
        JOIN category ch ON c.parent_id = ch.id
		)
        SELECT TOP 10 * FROM tblProduct WHERE tblProduct.categoryId IN (SELECT id FROM category) ORDER BY NEWId();
        RETURN;
    END

    CREATE TABLE #result (value varchar(MAX));
    
    WITH tf AS (
		SELECT b.keyword, COUNT(s.value) * 1.0 / NULLIF((SELECT COUNT(*) FROM STRING_SPLIT(@query, ' ')), 0) AS TF
        FROM STRING_SPLIT(@query, ' ') s
        RIGHT JOIN tblBaseVector b ON b.keyword = s.value
        GROUP BY b.keyword
	),
	df AS (
    --SELECT s.value AS keyword, COUNT(*) AS DF FROM STRING_SPLIT(@query, ' ') s GROUP BY s.value
	SELECT keyword, COUNT(DISTINCT id) AS DF 
    FROM (
        SELECT 
            id,
            TRIM(s.value) AS keyword
        FROM tblProduct
        CROSS APPLY STRING_SPLIT(LOWER(tblProduct.description), ' ') s
        
        UNION
        
        SELECT 
            id,
            TRIM(s.value) AS keyword
        FROM tblProduct
        CROSS APPLY STRING_SPLIT(LOWER(tblProduct.name), ' ') s
    ) tbl
    GROUP BY keyword
	),
	tfidf AS (
		SELECT 
			tf.keyword, 
			tf.TF * LOG((SELECT COUNT(*) FROM tblProduct) / NULLIF(df.DF, 0)) AS TFIdF
		FROM tf
		JOIN df ON tf.keyword = df.keyword
	),
	vectors AS (
    SELECT
        STRING_AGG(CAST(COALESCE(tfidf, 0) AS NVARCHAR), ',') WITHIN GROUP (ORDER BY tblBaseVector.keyword) AS vector
    FROM tblBaseVector
    LEFT JOIN tfidf ON tblBaseVector.keyword = tfidf.keyword AND tblBaseVector.keyword = tfidf.keyword
	)
	INSERT INTO #result
    SELECT * FROM vectors;

    --SELECT * FROM STRING_SPLIT((SELECT value FROM #result), ',');

	WITH ItemVector AS (
		SELECT v.productId AS id, s.value AS tfidf_value, 
           ROW_NUMBER() OVER (PARTITION BY v.productId ORDER BY (SELECT NULL)) AS pos
		FROM tblVector v
		CROSS APPLY STRING_SPLIT(v.vector, ',') s
	),
	QueryVector AS (
		SELECT s.value AS tfidf_value, ROW_NUMBER() OVER (ORDER BY (SELECT NULL)) AS pos
		FROM STRING_SPLIT((SELECT value FROM #result), ',') s
	),
	recommendation AS(
		SELECT iv.id,
		SUM(CAST(qv.tfidf_value AS FLOAT) * CAST(iv.tfidf_value AS FLOAT)) /
		NULLIF((SQRT(SUM(POWER(CAST(iv.tfidf_value AS FLOAT), 2))) * SQRT(SUM(POWER(CAST(qv.tfidf_value AS FLOAT), 2)))), 0) AS similarity
		FROM ItemVector iv
		JOIN QueryVector qv ON iv.pos = qv.pos
		GROUP BY iv.id
	),
	category AS (
        SELECT id FROM tblCategory WHERE id = @category
        UNION ALL
        SELECT c.id FROM tblCategory c
        JOIN category ch ON c.parent_id = ch.id
    )
	SELECT tblProduct.*
	FROM tblProduct
	JOIN recommendation ON tblProduct.id = recommendation.id
	WHERE recommendation.similarity != 0 AND tblProduct.categoryId IN (SELECT id FROM category)
	ORDER BY recommendation.similarity DESC
	OFFSET @page * 10 ROWS
    FETCH NEXT 10 ROWS ONLY

    DROP TABLE #result;
END;
GO

INSERT INTO tblResourceMap
VALUES
('shop_js', 'shop.js'),
('test_png', 'test.png'),
('main_css', 'main.css'),
('main_js', 'main.js'),
('test_js', 'test.js'),
('chat_js', 'chat.js'),
('admin_js', 'admin.js'),
('admin_css', 'admin.css'),
('log_js', 'log.js'),
('product_js', 'product.js'),
('chart_js', 'customChart.js'),
('userMain_css', 'userMain.css'),
('login_css', 'login.css'),
('cp_js', 'controlPanel.js'),
('filter_js', 'filter.js'),
('checkout_css','checkout.css'),
('userMain_js','userMain.js'),
('catalog_css','catalog.css'),
('catalog_js','catalog.js'),
('productInfoTest_css','productInfoTest.css'),
('productInfoTest_js','productInfoTest.js');

INSERT INTO tblUser (email, username, phoneNumber, password, persistentCookie, googleId, facebookId, isAdmin, profileStringResourceId)
VALUES 
('abc@example.com', 'user', '00000', 'user', NULL, NULL, NULL, 0, 'test_png'),
('abc1@example.com', 'admin', '00001', 'admin', NULL, NULL, NULL, 1, 'test_png'),
('abc2@example.com', 'user123', '05602', 'user', NULL, NULL, NULL, 0, 'test_png'),
('abc3@example.com', 'user33443', '12003', 'user', NULL, NULL, NULL, 0, 'test_png'),
('abc4@example.com', 'user126543', '56004', 'user', NULL, NULL, NULL, 0, 'test_png'),
('abc5@example.com', 'user0', '14778', 'user', NULL, NULL, NULL, 0, 'test_png');

--cosmestics and fashion
INSERT INTO tblCategory (name, imageStringResourceId, parent_id)
VALUES
	('All', 'test_png', NULL), -- 0
	('Fashion','test_png',0), --1
	('Electronics','test_png',0), --2
	('Furniture','test_png',0), --3
	('Cosmestic','test_png',0), --4
	('Book','test_png',0), --5
	('Man Fashion','test_png',1), --6
	('Woman Fashion','test_png',1), --7
	('Shoes','test_png',1), --8
	('Accessory','test_png',1), --9
	('T-Shirt','test_png',6), -- 10
	('Blazer','test_png',6), -- 11
	('Hoodie','test_png',6), -- 12
	('Shirt','test_png',6), -- 13
	('Jacket','test_png',6), -- 14
	('Coat','test_png',6), -- 15
	('Polo Shirt','test_png',6), -- 16
	('Man Jean','test_png',6), -- 17
	('Short','test_png',6), -- 18
	('Trouser','test_png',6), -- 19
	('Skincare','test_png',4), -- 20
	('Makeup','test_png',4), -- 21
	('Haircare','test_png',4), -- 22
	('Bodycare','test_png',4), -- 23
	('Fragrance','test_png',4); -- 24
INSERT INTO tblCategory (name, imageStringResourceId, parent_id)
VALUES
	-- skincare
	('Cleansers', 'test_png', (select id from tblCategory where name = 'Skincare')),
    ('Face_wash', 'test_png', (select id from tblCategory where name = 'Skincare')),
    ('Makeup_remover', 'test_png', (select id from tblCategory where name = 'Skincare')),
	-- makeup 
    ('Foundation', 'test_png', (select id from tblCategory where name = 'Makeup')),
    ('Concealer', 'test_png', (select id from tblCategory where name = 'Makeup')),
    ('Powder', 'test_png', (select id from tblCategory where name = 'Makeup')),
    ('Blush', 'test_png', (select id from tblCategory where name = 'Makeup')),
	-- Haircare 
    ('Shampoo', 'test_png', (select id from tblCategory where name = 'Haircare')),
    ('Conditioner', 'test_png', (select id from tblCategory where name = 'Haircare')),
    ('Dry_shampoo', 'test_png', (select id from tblCategory where name = 'Haircare')),
    ('Hair oil', 'test_png', (select id from tblCategory where name = 'Haircare')),
	-- Bodycare 
    ('Body wash', 'test_png', (select id from tblCategory where name = 'Bodycare')),
    ('Body scrub', 'test_png', (select id from tblCategory where name = 'Bodycare')),
    ('Body lotion', 'test_png', (select id from tblCategory where name = 'Bodycare')),
    ('Body butter', 'test_png', (select id from tblCategory where name = 'Bodycare')),
	-- Fragrance 
    ('Perfume', 'test_png', (select id from tblCategory where name = 'Fragrance')),
    ('Eau de toilette', 'test_png', (select id from tblCategory where name = 'Fragrance')),
    ('Body mist', 'test_png', (select id from tblCategory where name = 'Fragrance')),
    ('Essential oil', 'test_png', (select id from tblCategory where name = 'Fragrance')),
	--book
    ('Fiction', 'test_png', (select id from tblCategory where name = 'Book')),
    ('Non_fiction', 'test_png', (select id from tblCategory where name = 'Book')),
    ('Children', 'test_png', (select id from tblCategory where name = 'Book')),
    ('Comics_manga', 'test_png', (select id from tblCategory where name = 'Book'));
INSERT INTO tblCategory (name, imageStringResourceId, parent_id)
VALUES
    -- Fiction subcategories
    ('Fantasy', 'test_png', (select id from tblCategory where name = 'Fiction')),
    ('Mystery', 'test_png', (select id from tblCategory where name = 'Fiction')),
    ('Romance', 'test_png', (select id from tblCategory where name = 'Fiction')),
    ('Thriller', 'test_png', (select id from tblCategory where name = 'Fiction')),

    -- Non-Fiction subcategories
    ('Biography', 'test_png', (select id from tblCategory where name = 'Non_fiction')),
    ('Memoir', 'test_png', (select id from tblCategory where name = 'Non_fiction')),
    ('History', 'test_png', (select id from tblCategory where name = 'Non_fiction')),
    ('Philosophy', 'test_png', (select id from tblCategory where name = 'Non_fiction')),

    -- Children subcategories
    ('Picture_books', 'test_png', (select id from tblCategory where name = 'Children')),
    ('Early readers', 'test_png', (select id from tblCategory where name = 'Children')),
    ('Middle grade', 'test_png', (select id from tblCategory where name = 'Children')),
    ('Young adult', 'test_png', (select id from tblCategory where name = 'Children')),

    -- Comics & Manga subcategories
    ('Graphic novels', 'test_png', (select id from tblCategory where name = 'Comics_manga')),
    ('Manga', 'test_png', (select id from tblCategory where name = 'Comics_manga')),
    ('Superhero comics', 'test_png', (select id from tblCategory where name = 'Comics_manga')),

	--furniture
    ('Seating', 'test_png', (select id from tblCategory where name = 'Furniture')),
    ('Sleeping', 'test_png', (select id from tblCategory where name = 'Furniture')),
    ('Storage', 'test_png', (select id from tblCategory where name = 'Furniture')),
    ('Dining', 'test_png', (select id from tblCategory where name = 'Furniture')),
    ('Office', 'test_png', (select id from tblCategory where name = 'Furniture'));
INSERT INTO tblCategory (name, imageStringResourceId, parent_id)
VALUES
    -- seating subcategories
    ('Chairs', 'test_png', (select id from tblCategory where name = 'Seating')),
    ('Sofas and Couches', 'test_png', (select id from tblCategory where name = 'Seating')),
    ('Ottomans and Footstools:', 'test_png', (select id from tblCategory where name = 'Seating')),

    -- sleeping subcategories
    ('Beds', 'test_png', (select id from tblCategory where name = 'Sleeping')),
    ('Mattresses', 'test_png', (select id from tblCategory where name = 'Sleeping')),
    ('Pillows', 'test_png', (select id from tblCategory where name = 'Sleeping')),

    -- storage subcategories
    ('Cabinets', 'test_png', (select id from tblCategory where name = 'Storage')),
    ('Chests', 'test_png', (select id from tblCategory where name = 'Storage')),
    ('Trunks', 'test_png', (select id from tblCategory where name = 'Storage')),


    -- dining subcategories
    ('Benches', 'test_png', (select id from tblCategory where name = 'Dining')),
    ('Dining Tables', 'test_png', (select id from tblCategory where name = 'Dining')),
    ('Dining Chairs', 'test_png', (select id from tblCategory where name = 'Dining')),

    -- Office  subcategories
    ('Desks', 'test_png', (select id from tblCategory where name = 'Office')),
    ('Conference Tables', 'test_png', (select id from tblCategory where name = 'Office')),
    ('Reception Furniture', 'test_png', (select id from tblCategory where name = 'Office'));

--electronics
INSERT INTO tblCategory (name, imageStringResourceId, parent_id) VALUES
('smartphones', 'test_png', (select id from tblCategory where name = 'Electronics')),
('laptops', 'test_png', (select id from tblCategory where name = 'Electronics')),
('tablets', 'test_png', (select id from tblCategory where name = 'Electronics')),
('cameras', 'test_png', (select id from tblCategory where name = 'Electronics')),
('televisions', 'test_png', (select id from tblCategory where name = 'Electronics')),
('audio device', 'test_png', (select id from tblCategory where name = 'Electronics'));


INSERT INTO tblVariation (categoryId,name,datatype,unit)
 VALUES 
 	(1,'color','string',NULL),
 	(6,'man clothes size','string',NULL),
 	(8,'shoe size','string',NULL),
 	(7,'woman clothes size', 'string', NULL);


INSERT INTO tblShop (ownerId, name, address, profileStringResourceId, visible)
VALUES
(1, 'Gadget World', '789 Tech Road', 'test_png', 1),
(2, 'Sneaker Haven', '321 Fashion Blvd', 'test_png', 1),
(3, 'Home Essentials', '567 Home Lane', 'test_png', 1),
(4, 'Tech Universe', '123 Innovation St', 'test_png', 1),
(5, 'Fashion Hub', '456 Style Ave', 'test_png', 1),
(6, 'Fashion Path', N'154 Lê Lợi, Hải Châu, Đà Nẵng', 'test_png', 1);

-- Understand that being in here means that the promotion will be active
INSERT INTO tblPromotion (creatorId, name, type, ofAdmin, value, expireDate)
VALUES
(1, 'Black Friday Sale', 0, 0, 15, '2025-11-29'),
(3, 'New Year Offer', 1, 0, 75000, '2025-12-31'),
(2, 'Buy 1 Get 1', 0, 1, 50, '2025-12-31'),
(4, 'Summer Discount', 0, 0, 20, '2025-06-30'),
(5, 'Winter Clearance', 1, 1, 10000, '2025-12-15');

-- TODO: Match the category id here
INSERT INTO tblProduct (shopId, categoryId, name, description, availablePromotionId, imageStringResourceId, status)
VALUES
(1, (select id from tblCategory where name = 'smartphones'), 'Samsung Galaxy S22', 'Flagship Samsung smartphone', 1, 'test_png', 1),
(1, (select id from tblCategory where name = 'laptops'), 'MacBook Pro 14', 'Apple high-end laptop', 2, 'test_png', 1),
(2, (select id from tblCategory where name = 'Shoes'), 'Nike Air Max', 'Stylish and comfortable sneakers', 3, 'test_png', 1),
(2, (select id from tblCategory where name = 'Accessory'), 'Leather Handbag', 'Elegant leather handbag', NULL, 'test_png', 1),
(3, (select id from tblCategory where name = 'smartphones'), 'Air Fryer', 'Healthy cooking appliance', NULL, 'test_png', 1),
(3, (select id from tblCategory where name = 'Home Appliances'), 'Vacuum Cleaner', 'Powerful home cleaning device', NULL, 'test_png', 1),
(4, (select id from tblCategory where name = 'Electronics'), 'PlayStation 5', 'Next-gen gaming console', 4, 'test_png', 1),
(4, (select id from tblCategory where name = 'Electronics'), 'Xbox Series X', 'Powerful Microsoft gaming console', 4, 'test_png', 1),
(5, (select id from tblCategory where name = 'Sofa and Couches'), 'Modern Sofa', 'Comfortable and stylish', 5, 'test_png', 1),
(5, (select id from tblCategory where name = 'Dining Tables'), 'Wooden Dining Table', 'Elegant and durable', 5, 'test_png', 1),
(1, (select id from tblCategory where name = 'smartphones'), 'iPhone 14 Pro', 'Latest Apple smartphone', 1, 'test_png', 1),
(1, (select id from tblCategory where name = 'Shoes'), 'Adidas Ultraboost', 'High-performance running shoes', 3, 'test_png', 1),
(3, (select id from tblCategory where name = 'Electronics'), 'Microwave Oven', 'Efficient and modern', NULL, 'test_png', 1),
(4, (select id from tblCategory where name = 'Electronics'), 'Nintendo Switch', 'Portable gaming console', 4, 'test_png', 1),
(5, (select id from tblCategory where name = 'Beds'), 'Queen Size Bed', 'Luxurious and comfortable', 5, 'test_png', 1),
(1, (select id from tblCategory where name = 'smartphones'), 'Google Pixel 7', 'Latest Google smartphone', 1, 'test_png', 1),
(2, (select id from tblCategory where name = 'Shoes'), 'Puma Running Shoes', 'Lightweight and stylish', 3, 'test_png', 1),
(3, (select id from tblCategory where name = 'Home Appliances'), 'Blender', 'Powerful kitchen appliance', NULL, 'test_png', 1),
(4, (select id from tblCategory where name = 'laptops'), 'Gaming Laptop', 'High-end gaming performance', 4, 'test_png', 1),
(5, (select id from tblCategory where name = 'Chairs'), 'Office Chair', 'Ergonomic and comfortable', 5, 'test_png', 1),
(4, (select id from tblCategory where name = 'smartphones'), 'OnePlus 11', 'Flagship OnePlus smartphone', 1, 'test_png', 1),
(2, (select id from tblCategory where name = 'Shoes'), 'Reebok Sneakers', 'Durable and comfortable', 3, 'test_png', 1),
(3, (select id from tblCategory where name = 'Home Appliances'), 'Dishwasher', 'Efficient and modern', NULL, 'test_png', 1),
(4, (select id from tblCategory where name = 'televisions'), 'Smart TV', '4K Ultra HD', 4, 'test_png', 1),
(5, (select id from tblCategory where name = 'Storage'), 'Bookshelf', 'Modern wooden bookshelf', 5, 'test_png', 1),
(5, (select id from tblCategory where name = 'smartphones'), 'FlagShip Phone', 'A phone that is flagship, also, gaming', 5, 'test_png', 1),
(5, (select id from tblCategory where name = 'tablets'), 'FlagShip Tablet', 'Cool tablet', 5, 'test_png', 1);


INSERT INTO tblProduct (shopId, categoryId, name, description, availablePromotionId, imageStringResourceId, status)
VALUES
(6, 10, 'T-Shirt Path 1', 'Good', NULL, 'test_png', 1),
(6, 10, 'T-Shirt Path 2', 'Good', NULL, 'test_png', 1),
(6, 10, 'T-Shirt Path 3', 'Good', NULL, 'test_png', 1),
(6, 12, 'Hoodie Path 1', 'Good', NULL, 'test_png', 1),
(6, 11, 'Blazer Path 1', 'Good', NULL, 'test_png', 1);


INSERT INTO tblVariationValue (variationId, value)
  VALUES 
  	(1 , 'Black'),
  	(1 , 'White'),
  	(1 , 'Gray'),
  	(1 , 'Blue'),
  	(1 , 'Red'),
  	(1 , 'Beige'),
  	(1 , 'Brown'),
  	(1 , 'Navy'),
  	(1 , 'Pink'),
  	(1 , 'Orange'),
  	(1 , 'Green'),
  	(1 , 'Yellow'),
  	(2 , 'XS'),
  	(2 , 'S'),
  	(2 , 'M'),
  	(2 , 'L'),
  	(2 , 'XL'),
  	(2 , 'XXL'),
  	(3 , '35'),
  	(3 , '36'),
  	(3 , '37'),
  	(3 , '38'),
  	(3 , '39'),
  	(3 , '40'),
  	(3 , '41'),
  	(3 , '42'),
  	(3 , '43'),
  	(4 , 'XS'),
  	(4 , 'S'),
  	(4 , 'M'),
  	(4 , 'L'),
  	(4 , 'XL');


INSERT INTO tblProductItem (productId, stock, price)
VALUES
(5, 12, 1100),
(6, 8, 2000),
(7, 30, 250),
(8, 15, 180),
(9, 10, 300),
(10, 5, 500),
(11, 25, 1300),
(12, 20, 220),
(13, 18, 400),
(14, 10, 450),
(15, 8, 550),
(16, 12, 350),
(17, 5, 700),
(18, 6, 600),
(19, 7, 1200),
(20, 4, 1500),
(21, 15, 750),
(22, 20, 300),
(23, 10, 850),
(24, 5, 1800),
(25, 8, 950),
(1, 12, 500),
(2, 18, 650),
(3, 6, 1400),
(4, 9, 800);

INSERT INTO tblProductItem (productId, stock, price)
VALUES
(28, 12, 1100),
(28, 10, 1000),
(28, 5, 1200),
(29, 12, 1300),
(29, 20, 1400),
(30, 10, 1000),
(30, 9, 1000),
(31, 12, 1200),
(31, 13, 1200),
(31, 15, 1100),
(32, 10, 2000),
(32, 11, 2100),
(32, 12, 2200);

INSERT INTO tblProductCustomization(productItemId,variationValueId)
VALUES 
	(1, 1),
	(2, 15),
	(3, 1),
	(4, 16),
	(5, 2),
	(6, 16),
	(7, 3),
	(8, 17),
	(9, 3),
	(10, 16),
	(11, 4),
	(12, 15),
	(13, 4),
	(14, 16),
	(15, 8),
	(16, 15),
	(17, 3),
	(18, 15),
	(19, 3),
	(20, 16),
	(21, 1),
	(22, 15),
	(23, 1),
	(24, 16),
	(25, 1),
	(26, 17);

	--product item for electronics
INSERT INTO tblProductItem (productId, stock, price) VALUES
	(1, 42, 7200),
	(1, 58, 9100),
	(2, 96, 5300),
	(2, 62, 7800),
	(3, 47, 8600),
	(3, 38, 1400),
	(4, 69, 2500),
	(5, 55, 3800),
	(5, 24, 6700),
	(6, 81, 4100),
	(6, 49, 6300),
	(7, 39, 9200),
	(7, 76, 2700),
	(8, 53, 1500),
	(8, 21, 5300),
	(9, 30, 1800),
	(9, 71, 2500),
	(10, 19, 5700),
	(10, 35, 3200),
	(11, 61, 4600),
	(11, 44, 3900),
	(12, 22, 6800),
	(13, 47, 5700),
	(13, 27, 4400),
	(14, 66, 5900),
	(14, 38, 2200),
	(15, 32, 7100),
	(15, 55, 4700),
	(16, 41, 8200),
	(16, 68, 5300),
	(17, 35, 7600),
	(18, 73, 3400),
	(19, 58, 9100),
	(20, 24, 6500),
	(20, 45, 3800),
	(20, 48, 4800),
	(21, 52, 7200),
	(21, 60, 3100),
	(22, 30, 8600),
	(22, 19, 4900),
	(23, 48, 7500),
	(24, 22, 6900),
	(24, 53, 2800),
	(25, 77, 8100),
	(25, 29, 3900),
	(25, 20, 3800),
	(26, 34, 7300),
	(26, 46, 5000),
	(27, 39, 8700),
	(29, 61, 4200),
	(30, 45, 5900),
	(30, 26, 3100);	
	

INSERT INTO tblProductCustomization(productItemId,variationValueId)
 VALUES 
 	(27, 1),
 	(27, 15),
 	(28, 1),
 	(28, 16),
 	(29, 2),
 	(29, 16),
 	(30, 3),
 	(30, 17),
 	(31, 3),
 	(31, 16),
 	(32, 4),
 	(32, 15),
 	(33, 4),
 	(33, 16),
 	(34, 8),
 	(34, 15),
 	(35, 3),
 	(35, 15),
 	(36, 3),
 	(36, 16),
 	(37, 1),
 	(37, 15),
 	(38, 1),
 	(38, 16);
 
EXEC ComputeTFIdF

INSERT INTO tblServerStatistics (day, totalMoneyEarned, userNum, productNum, shopNum, promotionNum, purchaseNum, visitNum, peakSessionNum, averageResponseTime, maxResponseTime)
VALUES 
('2025-03-03',  5000, 120,  50,  10,  5,  10,  100, 30,  120, 500),
('2025-03-04',  1000, 123,  55,  12,  7,  12,  50, 70,  130, 700),
('2025-03-05',  4500, 127,  60,  18,  7,  17,  60, 70,  190, 490),
('2025-03-06',  8100, 160,  65,  19,  7,  8,  30, 50,  100, 1280),
('2025-03-07',  9000, 165,  70,  19,  9,  18,  50, 90,  105, 460),
('2025-03-08', 6000, 165,  90,  21, 18,  27,  150, 95,   90, 200),
('2025-03-09', 4000, 210,  90,  29, 19,  7,  200, 100,  85,  410),
('2025-03-10', 9800, 300,  160,  36, 26,  35,  200, 110,  80,  400),
('2025-03-11', 3400, 310,  200,  38, 28,  60,  50, 180,  55,  800),
('2025-03-12', 6000, 350, 210,  39, 28,  55,  170, 130,  70,  380);

-- Electronics variation, 
INSERT INTO tblVariation (categoryId, name, datatype, unit, status)
VALUES 
    ((SELECT id FROM tblCategory WHERE name = 'Electronics'), 'color', 'string', NULL, NULL),
    ((SELECT id FROM tblCategory WHERE name = 'Electronics'), 'storage capacity', 'string', 'GB', NULL),
    ((SELECT id FROM tblCategory WHERE name = 'Electronics'), 'RAM size', 'string', 'GB', NULL),
	((SELECT id FROM tblCategory WHERE name = 'Electronics'), 'brand', 'string', NULL, NULL),
	((SELECT id FROM tblCategory WHERE name = 'laptops'), 'chip', 'string', null, NULL),
	((SELECT id FROM tblCategory WHERE name = 'televisions'), 'screen size', 'string', 'inch', NULL),
    ((SELECT id FROM tblCategory WHERE name = 'cameras'), 'megapixels', 'string', 'MP', NULL);
-- Electronics variation value
	INSERT INTO tblVariationValue (variationId, value, status)
VALUES
    -- Color variations for Electronics
    ((SELECT id FROM tblVariation WHERE name = 'color' AND categoryId = (SELECT id FROM tblCategory WHERE name = 'Electronics')), 'Black', NULL),
    ((SELECT id FROM tblVariation WHERE name = 'color' AND categoryId = (SELECT id FROM tblCategory WHERE name = 'Electronics')), 'White', NULL),
    ((SELECT id FROM tblVariation WHERE name = 'color' AND categoryId = (SELECT id FROM tblCategory WHERE name = 'Electronics')), 'Silver', NULL),
    ((SELECT id FROM tblVariation WHERE name = 'color' AND categoryId = (SELECT id FROM tblCategory WHERE name = 'Electronics')), 'Gold', NULL),
	((SELECT id FROM tblVariation WHERE name = 'color' AND categoryId = (SELECT id FROM tblCategory WHERE name = 'Electronics')), 'Pink', NULL),
	((SELECT id FROM tblVariation WHERE name = 'color' AND categoryId = (SELECT id FROM tblCategory WHERE name = 'Electronics')), 'Cyan', NULL),
	((SELECT id FROM tblVariation WHERE name = 'color' AND categoryId = (SELECT id FROM tblCategory WHERE name = 'Electronics')), 'Purple', NULL),

	--storage capacity
	((SELECT id FROM tblVariation WHERE name = 'storage capacity' AND categoryId = (SELECT id FROM tblCategory WHERE name = 'Electronics')), '128GB', NULL),
	((SELECT id FROM tblVariation WHERE name = 'storage capacity' AND categoryId = (SELECT id FROM tblCategory WHERE name = 'Electronics')), '256GB', NULL),
	((SELECT id FROM tblVariation WHERE name = 'storage capacity' AND categoryId = (SELECT id FROM tblCategory WHERE name = 'Electronics')), '512GB', NULL),
	
	--RAM size
	((SELECT id FROM tblVariation WHERE name = 'RAM size' AND categoryId = (SELECT id FROM tblCategory WHERE name = 'Electronics')), '8GB', NULL),
	((SELECT id FROM tblVariation WHERE name = 'RAM size' AND categoryId = (SELECT id FROM tblCategory WHERE name = 'Electronics')), '16GB', NULL),
	((SELECT id FROM tblVariation WHERE name = 'RAM size' AND categoryId = (SELECT id FROM tblCategory WHERE name = 'Electronics')), '32GB', NULL),
	((SELECT id FROM tblVariation WHERE name = 'RAM size' AND categoryId = (SELECT id FROM tblCategory WHERE name = 'Electronics')), '64GB', NULL),


		--brand variation
	((SELECT id FROM tblVariation WHERE name = 'brand'), 'Apple', NULL),
    ((SELECT id FROM tblVariation WHERE name = 'brand'), 'Samsung', NULL),
    ((SELECT id FROM tblVariation WHERE name = 'brand'), 'Sony', NULL),
    ((SELECT id FROM tblVariation WHERE name = 'brand'), 'LG', NULL),
    ((SELECT id FROM tblVariation WHERE name = 'brand'), 'Dell', NULL),
    ((SELECT id FROM tblVariation WHERE name = 'brand'), 'HP', NULL),
    ((SELECT id FROM tblVariation WHERE name = 'brand'), 'Lenovo', NULL),
    ((SELECT id FROM tblVariation WHERE name = 'brand'), 'Asus', NULL),
    ((SELECT id FROM tblVariation WHERE name = 'brand'), 'Acer', NULL),
    ((SELECT id FROM tblVariation WHERE name = 'brand'), 'Canon', NULL),
    ((SELECT id FROM tblVariation WHERE name = 'brand'), 'Nikon', NULL);

	INSERT INTO tblVariationValue (variationId, value, status)
VALUES
	-- Screen size variations for Televisions
    ((SELECT id FROM tblVariation WHERE name = 'chip' AND categoryId = (SELECT id FROM tblCategory WHERE name = 'laptops')), 'AMD', NULL),
    ((SELECT id FROM tblVariation WHERE name = 'chip' AND categoryId = (SELECT id FROM tblCategory WHERE name = 'laptops')), 'Intel', NULL),

    -- Screen size variations for Televisions
    ((SELECT id FROM tblVariation WHERE name = 'screen size' AND categoryId = (SELECT id FROM tblCategory WHERE name = 'televisions')), '42 inch', NULL),
    ((SELECT id FROM tblVariation WHERE name = 'screen size' AND categoryId = (SELECT id FROM tblCategory WHERE name = 'televisions')), '50 inch', NULL),
    ((SELECT id FROM tblVariation WHERE name = 'screen size' AND categoryId = (SELECT id FROM tblCategory WHERE name = 'televisions')), '65 inch', NULL),

    -- Camera megapixel variations
    ((SELECT id FROM tblVariation WHERE name = 'megapixels' AND categoryId = (SELECT id FROM tblCategory WHERE name = 'cameras')), '12MP', NULL),
    ((SELECT id FROM tblVariation WHERE name = 'megapixels' AND categoryId = (SELECT id FROM tblCategory WHERE name = 'cameras')), '24MP', NULL),
    ((SELECT id FROM tblVariation WHERE name = 'megapixels' AND categoryId = (SELECT id FROM tblCategory WHERE name = 'cameras')), '48MP', NULL);


-- Book variation
INSERT INTO tblVariation (categoryId, name, datatype, unit, status)
VALUES 
    ((SELECT id FROM tblCategory WHERE name = 'Book'), 'Format', 'string', NULL, NULL),
    ((SELECT id FROM tblCategory WHERE name = 'Book'), 'Language', 'string', NULL, NULL),
    ((SELECT id FROM tblCategory WHERE name = 'Book'), 'Publication Year', 'integer', 'year', NULL),
    ((SELECT id FROM tblCategory WHERE name = 'Book'), 'Publisher', 'string', NULL, NULL),
	((SELECT id FROM tblCategory WHERE name = 'Book'), 'Rating', 'string', NULL, NULL);

--Book variation value
INSERT INTO tblVariationValue (variationId, value, status)
VALUES
    -- Giá trị cho "Format"
    ((SELECT id FROM tblVariation WHERE name = 'Format'), 'Hardcover', NULL),
    ((SELECT id FROM tblVariation WHERE name = 'Format'), 'Paperback', NULL),
    ((SELECT id FROM tblVariation WHERE name = 'Format'), 'Ebook', NULL),
    ((SELECT id FROM tblVariation WHERE name = 'Format'), 'Audiobook', NULL),

    -- Giá trị cho "Language"
    ((SELECT id FROM tblVariation WHERE name = 'Language'), 'English', NULL),
    ((SELECT id FROM tblVariation WHERE name = 'Language'), 'French', NULL),
    ((SELECT id FROM tblVariation WHERE name = 'Language'), 'German', NULL),
    ((SELECT id FROM tblVariation WHERE name = 'Language'), 'Spanish', NULL),
    ((SELECT id FROM tblVariation WHERE name = 'Language'), 'Chinese', NULL),
    ((SELECT id FROM tblVariation WHERE name = 'Language'), 'Japanese', NULL),

    -- Giá trị cho "Publication Year" (chỉ demo một số năm)
    ((SELECT id FROM tblVariation WHERE name = 'Publication Year'), '2020', NULL),
    ((SELECT id FROM tblVariation WHERE name = 'Publication Year'), '2021', NULL),
    ((SELECT id FROM tblVariation WHERE name = 'Publication Year'), '2022', NULL),
    ((SELECT id FROM tblVariation WHERE name = 'Publication Year'), '2023', NULL),

    -- Giá trị cho "Publisher"
    ((SELECT id FROM tblVariation WHERE name = 'Publisher'), 'Penguin Random House', NULL),
    ((SELECT id FROM tblVariation WHERE name = 'Publisher'), 'HarperCollins', NULL),
    ((SELECT id FROM tblVariation WHERE name = 'Publisher'), 'Simon & Schuster', NULL),
    ((SELECT id FROM tblVariation WHERE name = 'Publisher'), 'Hachette Book Group', NULL),
    ((SELECT id FROM tblVariation WHERE name = 'Publisher'), 'Macmillan Publishers', NULL),

    -- Giá trị cho "Rating"
    ((SELECT id FROM tblVariation WHERE name = 'Rating'), '1 Star', NULL),
    ((SELECT id FROM tblVariation WHERE name = 'Rating'), '2 Stars', NULL),
    ((SELECT id FROM tblVariation WHERE name = 'Rating'), '3 Stars', NULL),
    ((SELECT id FROM tblVariation WHERE name = 'Rating'), '4 Stars', NULL),
    ((SELECT id FROM tblVariation WHERE name = 'Rating'), '5 Stars', NULL);
SELECT * FROM tblProduct


SELECT * FROM tblVector


INSERT INTO tblUser (email, username, phoneNumber, password, persistentCookie, googleId, facebookId, isAdmin, profileStringResourceId)
VALUES 
('abc6@example.com', 'user2340', '34534', 'user', NULL, NULL, NULL, 0, 'test_png'),
('abc7@example.com', 'user4560', '85453', 'user', NULL, NULL, NULL, 0, 'test_png'),
('abc9@example.com', 'user5444', '45345', 'user', NULL, NULL, NULL, 0, 'test_png'),
('abc10@example.com', 'user9654', '45334', 'user', NULL, NULL, NULL, 0, 'test_png');


INSERT INTO tblShop (ownerId, name, address, profileStringResourceId, visible)
VALUES
(7, 'MB Cosmestic', N'154 Lac Long Quan, Thanh Khe, Da Nang', 'test_png', 1),
(8, 'DFG Furniture', N'23 Hai Phong, Thanh Khe, Da Nang', 'test_png', 1),
(9, 'Lovely Cosmestic', N'154 Thanh Hoa, Thanh Khe, Da Nang', 'test_png', 1),
(10, 'BNG Cosmestic', N'154 Au Co, Lien Chieu, Da Nang', 'test_png', 1);

 --cosmetic
INSERT INTO tblVariation (categoryId, name, datatype, unit)
VALUES
    ((SELECT id FROM tblCategory WHERE name = 'Cosmestic'), 'brand', 'string', NULL),
	((SELECT id FROM tblCategory WHERE name = 'Cosmestic'), 'volume', 'integer', 'ml'),

	((SELECT id FROM tblCategory WHERE name = 'Skincare'), 'skin type', 'string', NULL),

	((SELECT id FROM tblCategory WHERE name = 'Makeup'), 'finish & texture', 'string', NULL),

	((SELECT id FROM tblCategory WHERE name = 'Haircare'), 'hair type', 'string', NULL),
	((SELECT id FROM tblCategory WHERE name = 'Haircare'), 'form', 'string', NULL),

	((SELECT id FROM tblCategory WHERE name = 'Bodycare'), 'absorption rate', 'string', NULL),

	((SELECT id FROM tblCategory WHERE name = 'Fragrance'), 'fragrance notes', 'string', NULL);

	INSERT INTO tblVariationValue (variationId, value)
VALUES 
    -- brand
    ((select id from tblVariation where name = 'brand' and categoryId = (select id from tblCategory where name = 'Cosmestic')), 'mistine'), 
    ((select id from tblVariation where name = 'brand' and categoryId = (select id from tblCategory where name = 'Cosmestic')), 'maybelline'), 
    ((select id from tblVariation where name = 'brand' and categoryId = (select id from tblCategory where name = 'Cosmestic')), 'mac'), 
    ((select id from tblVariation where name = 'brand' and categoryId = (select id from tblCategory where name = 'Cosmestic')), 'estée lauder'),

    -- volume (ml)
    ((select id from tblVariation where name = 'volume' and categoryId = (select id from tblCategory where name = 'Cosmestic')), '10ml'),
    ((select id from tblVariation where name = 'volume' and categoryId = (select id from tblCategory where name = 'Cosmestic')), '15ml'),
    ((select id from tblVariation where name = 'volume' and categoryId = (select id from tblCategory where name = 'Cosmestic')), '30ml'),

    -- skin type
    ((select id from tblVariation where name = 'skin type' and categoryId = (select id from tblCategory where name = 'Skincare')), 'oily'),
    ((select id from tblVariation where name = 'skin type' and categoryId = (select id from tblCategory where name = 'Skincare')), 'dry'),
    ((select id from tblVariation where name = 'skin type' and categoryId = (select id from tblCategory where name = 'Skincare')), 'combo'),
    ((select id from tblVariation where name = 'skin type' and categoryId = (select id from tblCategory where name = 'Skincare')), 'sensitive'),

    -- finish & texture
    ((select id from tblVariation where name = 'finish & texture' and categoryId = (select id from tblCategory where name = 'Makeup')), 'matte'),
    ((select id from tblVariation where name = 'finish & texture' and categoryId = (select id from tblCategory where name = 'Makeup')), 'dewy'),
    ((select id from tblVariation where name = 'finish & texture' and categoryId = (select id from tblCategory where name = 'Makeup')), 'satin'),

    -- hair type
    ((select id from tblVariation where name = 'hair type' and categoryId = (select id from tblCategory where name = 'Haircare')), 'thin'),
    ((select id from tblVariation where name = 'hair type' and categoryId = (select id from tblCategory where name = 'Haircare')), 'thick'),
    ((select id from tblVariation where name = 'hair type' and categoryId = (select id from tblCategory where name = 'Haircare')), 'damaged'),

    -- absorption rate
    ((select id from tblVariation where name = 'absorption rate' and categoryId = (select id from tblCategory where name = 'Bodycare')), 'fast absorb'),
    ((select id from tblVariation where name = 'absorption rate' and categoryId = (select id from tblCategory where name = 'Bodycare')), 'deep moist'),

    -- fragrance notes
    ((select id from tblVariation where name = 'fragrance notes' and categoryId = (select id from tblCategory where name = 'Fragrance')), 'floral'),
    ((select id from tblVariation where name = 'fragrance notes' and categoryId = (select id from tblCategory where name = 'Fragrance')), 'woody'),
    ((select id from tblVariation where name = 'fragrance notes' and categoryId = (select id from tblCategory where name = 'Fragrance')), 'musky');

	INSERT INTO tblProduct (shopId, categoryId, name, description, availablePromotionId, imageStringResourceId, status)
VALUES
    -- Skincare Products
    (7, (SELECT id FROM tblCategory WHERE name = 'Cleansers'), 'Gentle Cleanser', 'Removes dirt and oil', NULL, 'test_png', 1),
    (7, (SELECT id FROM tblCategory WHERE name = 'Face_wash'), 'Foaming Face Wash', 'Deep cleansing', NULL, 'test_png', 1),
    (7, (SELECT id FROM tblCategory WHERE name = 'Makeup_remover'), 'Micellar Water', 'Removes makeup easily', NULL, 'test_png', 1),

    -- Makeup Products
    (9, (SELECT id FROM tblCategory WHERE name = 'Foundation'), 'Liquid Foundation', 'Provides smooth coverage', NULL, 'test_png', 1),
    (9, (SELECT id FROM tblCategory WHERE name = 'Concealer'), 'Full Coverage Concealer', 'Covers dark circles', NULL, 'test_png', 1),
    (9, (SELECT id FROM tblCategory WHERE name = 'Powder'), 'Matte Setting Powder', 'Long-lasting oil control', NULL, 'test_png', 1),
    (9, (SELECT id FROM tblCategory WHERE name = 'Blush'), 'Rosy Blush', 'Adds natural color', NULL, 'test_png', 1),

    -- Haircare Products
    (7, (SELECT id FROM tblCategory WHERE name = 'Shampoo'), 'Anti-Dandruff Shampoo', 'Fights dandruff', NULL, 'test_png', 1),
    (7, (SELECT id FROM tblCategory WHERE name = 'Conditioner'), 'Hydrating Conditioner', 'Softens hair', NULL, 'test_png', 1),
    (7, (SELECT id FROM tblCategory WHERE name = 'Dry_shampoo'), 'Volumizing Dry Shampoo', 'Absorbs oil', NULL, 'test_png', 1),
    (7, (SELECT id FROM tblCategory WHERE name = 'Hair oil'), 'Argan Hair Oil', 'Strengthens hair', NULL, 'test_png', 1),

    -- Bodycare Products
    (9, (SELECT id FROM tblCategory WHERE name = 'Body wash'), 'Moisturizing Body Wash', 'Nourishes skin', NULL, 'test_png', 1),
    (9, (SELECT id FROM tblCategory WHERE name = 'Body scrub'), 'Exfoliating Scrub', 'Removes dead skin cells', NULL, 'test_png', 1),
    (9, (SELECT id FROM tblCategory WHERE name = 'Body lotion'), 'Hydrating Lotion', 'Softens skin', NULL, 'test_png', 1),
    (9, (SELECT id FROM tblCategory WHERE name = 'Body butter'), 'Shea Body Butter', 'Intensive hydration', NULL, 'test_png', 1),

    -- Fragrance Products
    (9, (SELECT id FROM tblCategory WHERE name = 'Perfume'), 'Luxury Perfume', 'Long-lasting scent', NULL, 'test_png', 1),
    (9, (SELECT id FROM tblCategory WHERE name = 'Eau de toilette'), 'Fresh Eau de Toilette', 'Light and refreshing', NULL, 'test_png', 1),
    (9, (SELECT id FROM tblCategory WHERE name = 'Body mist'), 'Floral Body Mist', 'Delicate fragrance', NULL, 'test_png', 1),
    (9, (SELECT id FROM tblCategory WHERE name = 'Essential oil'), 'Lavender Essential Oil', 'Relaxing aroma', NULL, 'test_png', 1);

	INSERT INTO tblProductItem (productId, stock, price)
VALUES
    -- Skincare Products
    (25, 20, 120),
	(25, 17, 180),
    (26, 15, 150),
    (27, 25, 180),

    -- Makeup Products
    (28, 18, 300),
    (29, 22, 250),
    (30, 20, 220),
	(30, 43, 112),
	(30, 11, 167),
    (31, 16, 280),

    -- Haircare Products
    (32, 30, 180),
    (33, 28, 200),
    (34, 25, 230),
    (35, 12, 350),

    -- Bodycare Products
    (36, 15, 160),
    (37, 18, 200),
    (38, 22, 180),
    (39, 10, 300),

    -- Fragrance Products
    (40, 8, 1200),
    (41, 12, 800),
    (42, 20, 400),
    (43, 14, 500);

	INSERT INTO tblProductCustomization (productItemId, variationValueId)
VALUES 
    -- Skincare Products
    (39, (SELECT id FROM tblVariationValue WHERE value = 'sensitive')),
	(40, (SELECT id FROM tblVariationValue WHERE value = 'dry')),
    (41, (SELECT id FROM tblVariationValue WHERE value = 'oily')),
    (42, (SELECT id FROM tblVariationValue WHERE value = 'dry')),

    -- Makeup Products
    (43, (SELECT id FROM tblVariationValue WHERE value = 'matte')),
    (44, (SELECT id FROM tblVariationValue WHERE value = 'dewy')),
    (45, (SELECT id FROM tblVariationValue WHERE value = 'satin')),
	(46, (SELECT id FROM tblVariationValue WHERE value = 'matte')),
	(47, (SELECT id FROM tblVariationValue WHERE value = 'dewy')),
    (48, (SELECT id FROM tblVariationValue WHERE value = 'satin')),

    -- Haircare Products
    (49, (SELECT id FROM tblVariationValue WHERE value = 'damaged')),
    (50, (SELECT id FROM tblVariationValue WHERE value = 'thick')),
    (51, (SELECT id FROM tblVariationValue WHERE value = 'thick')),
    (52, (SELECT id FROM tblVariationValue WHERE value = 'thin')),

    -- Bodycare Products
    (53, (SELECT id FROM tblVariationValue WHERE value = 'fast absorb')),
    (54, (SELECT id FROM tblVariationValue WHERE value = '10ml')),
    (55, (SELECT id FROM tblVariationValue WHERE value = 'estée lauder')),
    (56, (SELECT id FROM tblVariationValue WHERE value = 'deep moist')),

    -- Fragrance Products
    (57, (SELECT id FROM tblVariationValue WHERE value = 'floral')),
    (57, (SELECT id FROM tblVariationValue WHERE value = 'woody')),
    (59, (SELECT id FROM tblVariationValue WHERE value = '30ml')),
    (60, (SELECT id FROM tblVariationValue WHERE value = 'musky'));

--furniture
INSERT INTO tblVariation (categoryId, name, datatype, unit)
VALUES
	--furniture
	((SELECT id FROM tblCategory WHERE name = 'Furniture'), 'Material', 'string', NULL),
    ((SELECT id FROM tblCategory WHERE name = 'Furniture'), 'Style', 'string', NULL),
    ((SELECT id FROM tblCategory WHERE name = 'Furniture'), 'Size', 'string', NULL),

	-- Seating 
	((SELECT id FROM tblCategory WHERE name = 'Seating'), 'Cushion Type', 'string', NULL),
    
    -- Sleeping 
    ((SELECT id FROM tblCategory WHERE name = 'Sleeping'), 'Mattress Type', 'string', NULL),
    
    -- Storage
    ((SELECT id FROM tblCategory WHERE name = 'Storage'), 'Capacity', 'integer', 'liters'),

    -- Dining 
    ((SELECT id FROM tblCategory WHERE name = 'Dining'), 'Shape', 'string', NULL),

    -- Office 
    ((SELECT id FROM tblCategory WHERE name = 'Office'), 'Adjustability', 'string', NULL);


	INSERT INTO tblVariationValue (variationId, value)
VALUES 
    -- Material
    ((SELECT id FROM tblVariation WHERE name = 'Material' AND categoryId = (SELECT id FROM tblCategory WHERE name = 'Furniture')), 'Metal'),
    ((SELECT id FROM tblVariation WHERE name = 'Material' AND categoryId = (SELECT id FROM tblCategory WHERE name = 'Furniture')), 'Plastic'),
    ((SELECT id FROM tblVariation WHERE name = 'Material' AND categoryId = (SELECT id FROM tblCategory WHERE name = 'Furniture')), 'Glass'),

    -- Style 
    ((SELECT id FROM tblVariation WHERE name = 'Style' AND categoryId = (SELECT id FROM tblCategory WHERE name = 'Furniture')), 'Industrial'),
    ((SELECT id FROM tblVariation WHERE name = 'Style' AND categoryId = (SELECT id FROM tblCategory WHERE name = 'Furniture')), 'Minimalist'),
    ((SELECT id FROM tblVariation WHERE name = 'Style' AND categoryId = (SELECT id FROM tblCategory WHERE name = 'Furniture')), 'Rustic'),

    -- Size 
    ((SELECT id FROM tblVariation WHERE name = 'Size' AND categoryId = (SELECT id FROM tblCategory WHERE name = 'Furniture')), 'Small'),
    ((SELECT id FROM tblVariation WHERE name = 'Size' AND categoryId = (SELECT id FROM tblCategory WHERE name = 'Furniture')), 'Medium'),
    ((SELECT id FROM tblVariation WHERE name = 'Size' AND categoryId = (SELECT id FROM tblCategory WHERE name = 'Furniture')), 'Large'),

    -- Cushion Type
    ((SELECT id FROM tblVariation WHERE name = 'Cushion Type' AND categoryId = (SELECT id FROM tblCategory WHERE name = 'Seating')), 'Feather'),
    ((SELECT id FROM tblVariation WHERE name = 'Cushion Type' AND categoryId = (SELECT id FROM tblCategory WHERE name = 'Seating')), 'Polyester'),

    -- Mattress Type
    ((SELECT id FROM tblVariation WHERE name = 'Mattress Type' AND categoryId = (SELECT id FROM tblCategory WHERE name = 'Sleeping')), 'Hybrid'),
    ((SELECT id FROM tblVariation WHERE name = 'Mattress Type' AND categoryId = (SELECT id FROM tblCategory WHERE name = 'Sleeping')), 'Innerspring'),
    ((SELECT id FROM tblVariation WHERE name = 'Mattress Type' AND categoryId = (SELECT id FROM tblCategory WHERE name = 'Sleeping')), 'Latex'),

    -- Capacity (Storage)
    ((SELECT id FROM tblVariation WHERE name = 'Capacity' AND categoryId = (SELECT id FROM tblCategory WHERE name = 'Storage')), '50'),
    ((SELECT id FROM tblVariation WHERE name = 'Capacity' AND categoryId = (SELECT id FROM tblCategory WHERE name = 'Storage')), '100'),
    ((SELECT id FROM tblVariation WHERE name = 'Capacity' AND categoryId = (SELECT id FROM tblCategory WHERE name = 'Storage')), '200'),

    -- Shape (Dining)
    ((SELECT id FROM tblVariation WHERE name = 'Shape' AND categoryId = (SELECT id FROM tblCategory WHERE name = 'Dining')), 'Square'),
    ((SELECT id FROM tblVariation WHERE name = 'Shape' AND categoryId = (SELECT id FROM tblCategory WHERE name = 'Dining')), 'Oval'),

    -- Adjustability (Office)
    ((SELECT id FROM tblVariation WHERE name = 'Adjustability' AND categoryId = (SELECT id FROM tblCategory WHERE name = 'Office')), 'Fixed'),
    ((SELECT id FROM tblVariation WHERE name = 'Adjustability' AND categoryId = (SELECT id FROM tblCategory WHERE name = 'Office')), 'Height'),
    ((SELECT id FROM tblVariation WHERE name = 'Adjustability' AND categoryId = (SELECT id FROM tblCategory WHERE name = 'Office')), 'Tilt');

	INSERT INTO tblProduct (shopId, categoryId, name, description, availablePromotionId, imageStringResourceId, status)
VALUES
    -- Seating Products
    (8, (SELECT id FROM tblCategory WHERE name = 'Chairs'), 'Ergonomic Office Chair', 'Comfortable for long hours', NULL, 'test_png', 1),
    (8, (SELECT id FROM tblCategory WHERE name = 'Sofas and Couches'), 'Luxury Leather Sofa', 'Spacious and stylish', NULL, 'test_png', 1),
    (8, (SELECT id FROM tblCategory WHERE name = 'Ottomans and Footstools'), 'Velvet Ottoman', 'Multi-purpose seating', NULL, 'test_png', 1),

    -- Sleeping Products
    (8, (SELECT id FROM tblCategory WHERE name = 'Beds'), 'King Size Bed', 'Solid wood frame', NULL, 'test_png', 1),
    (8, (SELECT id FROM tblCategory WHERE name = 'Mattresses'), 'Memory Foam Mattress', 'Pressure-relieving comfort', NULL, 'test_png', 1),
    (8, (SELECT id FROM tblCategory WHERE name = 'Pillows'), 'Cooling Gel Pillow', 'Optimal neck support', NULL, 'test_png', 1),

    -- Storage Products
    (8, (SELECT id FROM tblCategory WHERE name = 'Cabinets'), 'Modern Storage Cabinet', 'Ample storage space', NULL, 'test_png', 1),
    (8, (SELECT id FROM tblCategory WHERE name = 'Chests'), 'Antique Wooden Chest', 'Classic and durable', NULL, 'test_png', 1),
    (8, (SELECT id FROM tblCategory WHERE name = 'Trunks'), 'Vintage Travel Trunk', 'Rustic decorative piece', NULL, 'test_png', 1),

    -- Dining Products
    (8, (SELECT id FROM tblCategory WHERE name = 'Benches'), 'Farmhouse Dining Bench', 'Rustic wooden finish', NULL, 'test_png', 1),
    (8, (SELECT id FROM tblCategory WHERE name = 'Dining Tables'), 'Extendable Dining Table', 'Seats up to 8 people', NULL, 'test_png', 1),
    (8, (SELECT id FROM tblCategory WHERE name = 'Dining Chairs'), 'Upholstered Dining Chair', 'Elegant design', NULL, 'test_png', 1),

    -- Office Products
    (8, (SELECT id FROM tblCategory WHERE name = 'Desks'), 'Standing Desk', 'Adjustable height', NULL, 'test_png', 1),
    (8, (SELECT id FROM tblCategory WHERE name = 'Conference Tables'), 'Executive Conference Table', 'Spacious and modern', NULL, 'test_png', 1),
    (8, (SELECT id FROM tblCategory WHERE name = 'Reception Furniture'), 'Lobby Reception Desk', 'Professional and sleek', NULL, 'test_png', 1);


	INSERT INTO tblProductItem (productId, stock, price)
VALUES


    -- Seating Products
    ((SELECT id FROM tblProduct WHERE name = 'Ergonomic Office Chair'), 10, 2500),
    ((SELECT id FROM tblProduct WHERE name = 'Luxury Leather Sofa'), 5, 8500),
    ((SELECT id FROM tblProduct WHERE name = 'Velvet Ottoman'), 12, 1800),
	((SELECT id FROM tblProduct WHERE name = 'Velvet Ottoman'), 54, 1655),

    -- Sleeping Products
    ((SELECT id FROM tblProduct WHERE name = 'King Size Bed'), 6, 7500),
    ((SELECT id FROM tblProduct WHERE name = 'Memory Foam Mattress'), 8, 4200),
    ((SELECT id FROM tblProduct WHERE name = 'Cooling Gel Pillow'), 20, 800),

    -- Storage Products
    ((SELECT id FROM tblProduct WHERE name = 'Modern Storage Cabinet'), 15, 3200),
    ((SELECT id FROM tblProduct WHERE name = 'Antique Wooden Chest'), 10, 5000),
    ((SELECT id FROM tblProduct WHERE name = 'Vintage Travel Trunk'), 8, 4500),

    -- Dining Products
    ((SELECT id FROM tblProduct WHERE name = 'Farmhouse Dining Bench'), 10, 2800),
    ((SELECT id FROM tblProduct WHERE name = 'Extendable Dining Table'), 5, 6500),
    ((SELECT id FROM tblProduct WHERE name = 'Upholstered Dining Chair'), 12, 2200),

    -- Office Products
    ((SELECT id FROM tblProduct WHERE name = 'Standing Desk'), 10, 4800),
    ((SELECT id FROM tblProduct WHERE name = 'Executive Conference Table'), 3, 9000),
    ((SELECT id FROM tblProduct WHERE name = 'Lobby Reception Desk'), 4, 11000),
	((SELECT id FROM tblProduct WHERE name = 'Lobby Reception Desk'), 322, 5456);

	INSERT INTO tblProductCustomization (productItemId, variationValueId)
VALUES
    -- Seating Products
    (61, 
     (SELECT id FROM tblVariationValue WHERE value = 'Polyester')),
    (62, 
     (SELECT id FROM tblVariationValue WHERE value = 'Feather')),
    (63, 
     (SELECT id FROM tblVariationValue WHERE value = 'Rustic')),



    -- Sleeping Products
    (64, 
     (SELECT id FROM tblVariationValue WHERE value = 'Large')),
    (65, 
     (SELECT id FROM tblVariationValue WHERE value = 'Hybrid')),
    (66, 
     (SELECT id FROM tblVariationValue WHERE value = 'Latex')),

    -- Storage Products
    (67, 
     (SELECT id FROM tblVariationValue WHERE value = '100')),
    (68, 
     (SELECT id FROM tblVariationValue WHERE value = '50')),
    (69, 
     (SELECT id FROM tblVariationValue WHERE value = '200')),

    -- Dining Products
    (70, 
     (SELECT id FROM tblVariationValue WHERE value = 'Industrial')),
    (71, 
     (SELECT id FROM tblVariationValue WHERE value = 'Oval')),
    (72, 
     (SELECT id FROM tblVariationValue WHERE value = 'Minimalist')),

    -- Office Products
    (73, 
     (SELECT id FROM tblVariationValue WHERE value = 'Height')),
    (74, 
     (SELECT id FROM tblVariationValue WHERE value = 'Fixed')),
	(75, 
     (SELECT id FROM tblVariationValue WHERE value = 'Tilt')),
	(63, 
     (SELECT id FROM tblVariationValue WHERE value = 'Minimalist')),
    (75, 
     (SELECT id FROM tblVariationValue WHERE value = 'Fixed'));

INSERT INTO tblProduct (shopId, categoryId, name, description, availablePromotionId, imageStringResourceId, status)
VALUES
(1, (select id from tblCategory where name = 'smartphones'), 'Samsung Galaxy S24 Ultra', 'Flagship Samsung smartphone', 1, 'test_png', 1);

INSERT INTO tblVariation (categoryId, name, datatype, unit, status)
VALUES
(83, 'Storage Capacity', 'integer', 'GB', 1);

INSERT INTO tblVariationValue (variationId, value, status)
VALUES
(IDENT_CURRENT('tblVariation'), '64', 1),
(IDENT_CURRENT('tblVariation'), '128', 1),
(IDENT_CURRENT('tblVariation'), '256', 1);

INSERT INTO tblVariation (categoryId, name, datatype, unit, status)
VALUES
(83, 'Color', 'string', NULL, 1); -- might screw things up

INSERT INTO tblVariationValue (variationId, value, status)
VALUES
(IDENT_CURRENT('tblVariation'), 'Silver', 1),
(IDENT_CURRENT('tblVariation'), 'Black', 1),
(IDENT_CURRENT('tblVariation'), 'White', 1);

INSERT INTO tblProductItem (productId, stock, price)
VALUES
(IDENT_CURRENT('tblProduct'), 11, 128338),
(IDENT_CURRENT('tblProduct'), 20, 2168419),
(IDENT_CURRENT('tblProduct'), 2, 14236173127),
(IDENT_CURRENT('tblProduct'), 7, 2672123123),
(IDENT_CURRENT('tblProduct'), 14, 75243422);

INSERT INTO tblProductCustomization (productItemId, variationValueId)
VALUES
(IDENT_CURRENT('tblProductItem') - 4, IDENT_CURRENT('tblVariationValue') - 5), -- 64
(IDENT_CURRENT('tblProductItem') - 4, IDENT_CURRENT('tblVariationValue') - 2), -- silver

(IDENT_CURRENT('tblProductItem') - 3, IDENT_CURRENT('tblVariationValue') - 5), -- 64
(IDENT_CURRENT('tblProductItem') - 3, IDENT_CURRENT('tblVariationValue')), -- white

(IDENT_CURRENT('tblProductItem') - 2, IDENT_CURRENT('tblVariationValue') - 4), -- 128
(IDENT_CURRENT('tblProductItem') - 2, IDENT_CURRENT('tblVariationValue') - 2), -- silver

(IDENT_CURRENT('tblProductItem') - 1, IDENT_CURRENT('tblVariationValue') - 4), -- 128
(IDENT_CURRENT('tblProductItem') - 1, IDENT_CURRENT('tblVariationValue') - 1), -- black

(IDENT_CURRENT('tblProductItem'), IDENT_CURRENT('tblVariationValue') - 3), -- 256
(IDENT_CURRENT('tblProductItem'), IDENT_CURRENT('tblVariationValue') - 2); -- silver


--FIX Some product with null category
update tblProduct
set categoryId = 3
where categoryId is NULL;
