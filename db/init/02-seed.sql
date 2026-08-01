INSERT INTO users (
    name,
    email,
    password,
    birth_date,
    first_name,
    last_name,
    address1,
    country,
    zipcode,
    state,
    city,
    mobile_phone
)   VALUES
        ('testUser1', 'test_user_1@gmail.com', 'Qwer1234', NULL, 'One', 'Testuser', 'test_user_1_address_1', 'India', '123456', 'Bangalore st.', 'Bangalore city', '+71234567890'),
        ('testUser2', 'test_user_2@gmail.com', 'Qwer1234', CURRENT_DATE, 'Two', 'Testuser', 'test_user_2_address_2', 'Israel', '09876543', 'Tel-Aviv st.', 'Tel-Aviv city', '+666666666666'),
        ('testUser3', 'test_user_3@gmail.com', 'Qwer1234', '2001-01-01', 'Three', 'Testuser', 'test_user_3_address_3', 'Canada', '1378of0', 'Manitoba st.', 'Ontario city', '+1-123-456-0000');