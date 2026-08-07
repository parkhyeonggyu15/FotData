import { useState } from "react";

interface TeamCrestProps {
  src: string | null;
  alt: string;
  size?: number;
}

export function TeamCrest({ src, alt, size = 20 }: TeamCrestProps) {
  const [failed, setFailed] = useState(false);

  if (!src || failed) {
    return (
      <span
        className="team-crest team-crest-fallback"
        style={{ width: size, height: size }}
        aria-hidden="true"
      />
    );
  }

  return (
    <img
      className="team-crest"
      src={src}
      alt={alt}
      width={size}
      height={size}
      loading="lazy"
      onError={() => setFailed(true)}
    />
  );
}
