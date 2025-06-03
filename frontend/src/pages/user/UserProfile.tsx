import { useState } from "react";
import UserNavbar from "../../components/UserNavbar";

interface UserProfile {
  fullName: string;
  email: string;
  studentId: string;
  class: string;
  dob: string;
  phone: string;
}

export default function UserProfilePage() {
  // Dữ liệu mẫu tạm thời để không gọi API bị lỗi
  const [profile, setProfile] = useState<UserProfile>({
    fullName: "Nguyen Van A",
    email: "nguyenvana@example.com",
    studentId: "123456",
    class: "12A1",
    dob: "2000-01-01",
    phone: "0123456789",
  });

  const [editMode, setEditMode] = useState(false);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setProfile({ ...profile, [e.target.name]: e.target.value });
  };

  const handleSave = () => {
    alert("Save function is disabled temporarily.");
    setEditMode(false);
  };

  return (
    <>
      <title>My Profile</title>
      <UserNavbar selected="profile" />
      <div className="p-6 bg-gray-100 min-h-screen max-w-xl mx-auto">
        <h1 className="text-2xl font-bold mb-4 text-blue-700">User Profile</h1>

        <div className="space-y-4">
          {[
            "fullName",
            "email",
            "studentId",
            "class",
            "dob",
            "phone",
          ].map((field) => (
            <div key={field}>
              <label className="block font-medium capitalize mb-1">
                {field === "dob" ? "Date of Birth" : field.replace(/([A-Z])/g, " $1")}
              </label>
              <input
                type={field === "dob" ? "date" : "text"}
                name={field}
                value={(profile as any)[field]}
                disabled={!editMode}
                onChange={handleChange}
                className={`w-full px-3 py-2 border rounded ${
                  editMode ? "bg-white" : "bg-gray-100"
                }`}
              />
            </div>
          ))}

          <div className="flex space-x-4 mt-6">
            {editMode ? (
              <>
                <button
                  onClick={handleSave}
                  className="bg-green-600 text-white px-4 py-2 rounded hover:bg-green-700"
                >
                  Save
                </button>
                <button
                  onClick={() => setEditMode(false)}
                  className="bg-gray-400 text-white px-4 py-2 rounded hover:bg-gray-500"
                >
                  Cancel
                </button>
              </>
            ) : (
              <button
                onClick={() => setEditMode(true)}
                className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700"
              >
                Edit Profile
              </button>
            )}
          </div>
        </div>
      </div>
    </>
  );
}
