//
// User.swift
//
// Generated by openapi-generator
// https://openapi-generator.tech
//

import Foundation
#if canImport(AnyCodable)
import AnyCodable
#endif

@available(*, deprecated, renamed: "PetstoreClientAPI.User")
public typealias User = PetstoreClientAPI.User

extension PetstoreClientAPI {

public final class User: Codable, JSONEncodable, Hashable {

    public var id: Int64?
    public var username: String?
    public var firstName: String?
    public var lastName: String?
    public var email: String?
    public var password: String?
    public var phone: String?
    /** User Status */
    public var userStatus: Int?

    public init(id: Int64? = nil, username: String? = nil, firstName: String? = nil, lastName: String? = nil, email: String? = nil, password: String? = nil, phone: String? = nil, userStatus: Int? = nil) {
        self.id = id
        self.username = username
        self.firstName = firstName
        self.lastName = lastName
        self.email = email
        self.password = password
        self.phone = phone
        self.userStatus = userStatus
    }

    public enum CodingKeys: String, CodingKey, CaseIterable {
        case id
        case username
        case firstName
        case lastName
        case email
        case password
        case phone
        case userStatus
    }

    // Encodable protocol methods

    public func encode(to encoder: Encoder) throws {
        var container = encoder.container(keyedBy: CodingKeys.self)
        try container.encodeIfPresent(id, forKey: .id)
        try container.encodeIfPresent(username, forKey: .username)
        try container.encodeIfPresent(firstName, forKey: .firstName)
        try container.encodeIfPresent(lastName, forKey: .lastName)
        try container.encodeIfPresent(email, forKey: .email)
        try container.encodeIfPresent(password, forKey: .password)
        try container.encodeIfPresent(phone, forKey: .phone)
        try container.encodeIfPresent(userStatus, forKey: .userStatus)
    }

    public static func == (lhs: User, rhs: User) -> Bool {
        lhs.id == rhs.id &&
        lhs.username == rhs.username &&
        lhs.firstName == rhs.firstName &&
        lhs.lastName == rhs.lastName &&
        lhs.email == rhs.email &&
        lhs.password == rhs.password &&
        lhs.phone == rhs.phone &&
        lhs.userStatus == rhs.userStatus
        
    }

    public func hash(into hasher: inout Hasher) {
        hasher.combine(id?.hashValue)
        hasher.combine(username?.hashValue)
        hasher.combine(firstName?.hashValue)
        hasher.combine(lastName?.hashValue)
        hasher.combine(email?.hashValue)
        hasher.combine(password?.hashValue)
        hasher.combine(phone?.hashValue)
        hasher.combine(userStatus?.hashValue)
        
    }
}

}