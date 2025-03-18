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
ADD status bit;

CREATE TABLE tblVariationValue
(
	id int PRIMARY KEY IDENTITY(1, 1),
	variationId int,
	value varchar(15),

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

    CREATE TABLE #result (value varchar(400));
    
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
('filter_js', 'filter.js');

INSERT INTO tblUser (email, username, phoneNumber, password, persistentCookie, googleId, facebookId, isAdmin, profileStringResourceId)
VALUES 
('abc@example.com', 'user', '00000', 'user', NULL, NULL, NULL, 0, 'test_png'),
('abc1@example.com', 'admin', '00001', 'admin', NULL, NULL, NULL, 1, 'test_png'),
('abc2@example.com', 'user123', '05602', 'user', NULL, NULL, NULL, 0, 'test_png'),
('abc3@example.com', 'user33443', '12003', 'user', NULL, NULL, NULL, 0, 'test_png'),
('abc4@example.com', 'user126543', '56004', 'user', NULL, NULL, NULL, 0, 'test_png');

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


INSERT INTO tblShop (ownerId, name, address, profileStringResourceId, visible)
VALUES
(1, 'Gadget World', '789 Tech Road', 'test_png', 1),
(2, 'Sneaker Haven', '321 Fashion Blvd', 'test_png', 1),
(3, 'Home Essentials', '567 Home Lane', 'test_png', 1),
(4, 'Tech Universe', '123 Innovation St', 'test_png', 1),
(5, 'Fashion Hub', '456 Style Ave', 'test_png', 1);

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
(1, 3, 'Samsung Galaxy S22', 'Flagship Samsung smartphone', 1, 'test_png', 1),
(1, 4, 'MacBook Pro 14', 'Apple high-end laptop', 2, 'test_png', 1),
(2, 5, 'Nike Air Max', 'Stylish and comfortable sneakers', 3, 'test_png', 1),
(2, 6, 'Leather Handbag', 'Elegant leather handbag', NULL, 'test_png', 1),
(3, 7, 'Air Fryer', 'Healthy cooking appliance', NULL, 'test_png', 1),
(3, 7, 'Vacuum Cleaner', 'Powerful home cleaning device', NULL, 'test_png', 1),
(4, 8, 'PlayStation 5', 'Next-gen gaming console', 4, 'test_png', 1),
(4, 8, 'Xbox Series X', 'Powerful Microsoft gaming console', 4, 'test_png', 1),
(5, 9, 'Modern Sofa', 'Comfortable and stylish', 5, 'test_png', 1),
(5, 9, 'Wooden Dining Table', 'Elegant and durable', 5, 'test_png', 1),
(1, 3, 'iPhone 14 Pro', 'Latest Apple smartphone', 1, 'test_png', 1),
(1, 5, 'Adidas Ultraboost', 'High-performance running shoes', 3, 'test_png', 1),
(3, 7, 'Microwave Oven', 'Efficient and modern', NULL, 'test_png', 1),
(4, 8, 'Nintendo Switch', 'Portable gaming console', 4, 'test_png', 1),
(5, 9, 'Queen Size Bed', 'Luxurious and comfortable', 5, 'test_png', 1),
(1, 3, 'Google Pixel 7', 'Latest Google smartphone', 1, 'test_png', 1),
(2, 5, 'Puma Running Shoes', 'Lightweight and stylish', 3, 'test_png', 1),
(3, 7, 'Blender', 'Powerful kitchen appliance', NULL, 'test_png', 1),
(4, 8, 'Gaming Laptop', 'High-end gaming performance', 4, 'test_png', 1),
(5, 9, 'Office Chair', 'Ergonomic and comfortable', 5, 'test_png', 1),
(4, 3, 'OnePlus 11', 'Flagship OnePlus smartphone', 1, 'test_png', 1),
(2, 5, 'Reebok Sneakers', 'Durable and comfortable', 3, 'test_png', 1),
(3, 7, 'Dishwasher', 'Efficient and modern', NULL, 'test_png', 1),
(4, 8, 'Smart TV', '4K Ultra HD', 4, 'test_png', 1),
(5, 9, 'Bookshelf', 'Modern wooden bookshelf', 5, 'test_png', 1),
(5, 9, 'FlagShip Phone', 'A phone that is flagship, also, gaming', 5, 'test_png', 1),
(5, 9, 'FlagShip Tablet', 'Cool tablet', 5, 'test_png', 1);


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

SELECT * FROM tblProduct


SELECT * FROM tblVector

WITH category AS (SELECT id FROM tblCategory WHERE name = 'Fashion' UNION ALL SELECT c.id FROM tblCategory c JOIN category ch ON c.parent_id = ch.id) 
SELECT TOP 10 * FROM tblProduct WHERE tblProduct.shopId = 4 AND tblProduct.categoryId IN (SELECT id FROM category)