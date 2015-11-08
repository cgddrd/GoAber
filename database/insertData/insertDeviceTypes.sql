INSERT INTO UserRoles (type) VALUES ('administrator');
INSERT INTO Users (email, nickname, userRoleId) values ('myEmail', 'test', '1');
INSERT INTO DeviceTypes (name, tokenEndpoint, consumerKey, consumerSecret, clientId, authorizationEndpoint) values ('fitbit', 'https://api.fitbit.com/oauth2/token', 'e06d4e7dcbc6fc80c0d00b187b6fb2e1', 'bafe21eca0c10cfe54f21e9b685f041f', '229R69', 'https://www.fitbit.com/oauth2/authorize');
